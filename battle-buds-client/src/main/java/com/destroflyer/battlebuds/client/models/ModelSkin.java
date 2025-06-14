package com.destroflyer.battlebuds.client.models;

import com.destroflyer.battlebuds.client.FileAssets;
import com.destroflyer.battlebuds.client.JMonkeyUtil;
import com.destroflyer.battlebuds.shared.Util;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ModelSkin {

    private ModelSkin(String filePath) {
        loadFile(filePath);
    }
    private static ConcurrentHashMap<String, ModelSkin> cachedSkins = new ConcurrentHashMap<>();
    private static final String[] FILE_EXTENSIONS = new String[]{ "j3o", "mesh.xml", "blend" };
    private Element modelElement;
    private Element positionElement;
    private Element materialElement;
    private Element modifiersElement;
    private String name;
    private float modelNormScale;
    private Vector3f modelScale;
    private String rigType;
    private float materialAmbient;
    private LinkedList<ModelModifier> modelModifiers = new LinkedList<>();

    public static ModelSkin get(String filePath) {
        return cachedSkins.computeIfAbsent(filePath, fp -> new ModelSkin(filePath));
    }

    private void loadFile(String filePath) {
        try {
            Document document = new SAXBuilder().build(new File(FileAssets.ROOT + filePath));
            Element rootElement = document.getRootElement();
            name = rootElement.getAttributeValue("name");
            modelElement = rootElement.getChild("model");
            positionElement = modelElement.getChild("position");
            materialElement = modelElement.getChild("material");
            modifiersElement = modelElement.getChild("modifiers");
            modelNormScale = getAttributeValue(modelElement, "normScale", 1);
            modelScale = getAttributeValue(modelElement, "scale", Vector3f.UNIT_XYZ);
            rigType = modelElement.getAttributeValue("rigType");
            materialAmbient = getAttributeValue(materialElement, "ambient", 0.15f);
        } catch (JDOMException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean getAttributeValue(Element element, String attributeName, boolean defaultValue) {
        return (getAttributeValue(element, attributeName, (defaultValue ? 1 : 0)) == 1);
    }

    private float getAttributeValue(Element element, String attributeName, float defaultValue) {
        if (element != null) {
            Attribute attribute = element.getAttribute(attributeName);
            if (attribute != null) {
                try {
                    return attribute.getFloatValue();
                } catch (DataConversionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return defaultValue;
    }

    private Vector3f getAttributeValue(Element element, String attributeName, Vector3f defaultValue) {
        if (element != null) {
            Attribute attribute = element.getAttribute(attributeName);
            if (attribute != null) {
                String[] coordinates = attribute.getValue().split(",");
                if (coordinates.length == 3) {
                    float x = Float.parseFloat(coordinates[0]);
                    float y = Float.parseFloat(coordinates[1]);
                    float z = Float.parseFloat(coordinates[2]);
                    return new Vector3f(x, y, z);
                } else {
                    try {
                        float value = attribute.getFloatValue();
                        return new Vector3f(value, value, value);
                    } catch(DataConversionException ex) {
                    }
                }
            }
        }
        return defaultValue;
    }

    public Node load(AssetManager assetManager) {
        Node node = loadModel(assetManager);
        loadMaterial(assetManager, node);
        loadPosition(node);
        loadModifiers();
        applyGeometryInformation(node);
        return node;
    }

    private Node loadModel(AssetManager assetManager) {
        Node node = null;
        if (name != null) {
            String modelPath = getModelFilePath(name, name);
            if (modelPath != null) {
                node = (Node) assetManager.loadModel(modelPath);
            }
        }
        if (node == null) {
            node = new Node();
        }
        node.setLocalScale(modelScale.mult(modelNormScale));
        if (getAttributeValue(modelElement, "generateTangents", false)) {
            TangentBinormalGenerator.generate(node);
        }
        return node;
    }

    private String getModelFilePath(String modelName, String fileName) {
        for (String FILE_EXTENSION : FILE_EXTENSIONS) {
            String modelFilePath = getModelFilePath(modelName, fileName, FILE_EXTENSION);
            if (FileAssets.exists(modelFilePath)) {
                return modelFilePath;
            }
        }
        return null;
    }

    private String getModelFilePath(String modelName, String fileName, String fileExtension) {
        return "models/" + modelName + "/" + fileName + "." + fileExtension;
    }

    private void loadMaterial(AssetManager assetManager, Node node) {
        if (materialElement != null) {
            List<Element> materialElements = materialElement.getChildren();
            for (int i = 0; i < materialElements.size(); i++){
                Element currentMaterialElement = materialElements.get(i);
                String sourceName = currentMaterialElement.getAttributeValue("source", name);
                String materialDefintion = currentMaterialElement.getText();
                Material material = null;
                if (currentMaterialElement.getName().equals("color")) {
                    float[] colorComponents = Util.parseToFloatArray(materialDefintion.split(","));
                    ColorRGBA colorRGBA = new ColorRGBA(colorComponents[0], colorComponents[1], colorComponents[2], colorComponents[3]);
                    material = MaterialFactory.generateLightingMaterial(assetManager, colorRGBA);
                } else if(currentMaterialElement.getName().equals("texture")) {
                    String textureFilePath = (getResourcesFilePath(sourceName) + currentMaterialElement.getText());
                    material = MaterialFactory.generateLightingMaterial(assetManager, textureFilePath);
                    // [jME 3.1 SNAPSHOT] Hardware skinning currently doesn't seem to support normal maps correctly
                    if (!ModelObject.HARDWARE_SKINNING) {
                        tryLoadTexture(assetManager, material, "NormalMap", currentMaterialElement.getAttributeValue("normalMap"), sourceName);
                    }
                    tryLoadTexture(assetManager, material, "AlphaMap", currentMaterialElement.getAttributeValue("alphaMap"), sourceName);
                    tryLoadTexture(assetManager, material, "SpecularMap", currentMaterialElement.getAttributeValue("specularMap"), sourceName);
                    tryLoadTexture(assetManager, material, "GlowMap", currentMaterialElement.getAttributeValue("glowMap"), sourceName);
                }
                if (material != null) {
                    String filter = currentMaterialElement.getAttributeValue("filter", "bilinear");
                    if (filter.equals("nearest")) {
                        MaterialFactory.setFilter_Nearest(material);
                    }
                    Geometry child = (Geometry) node.getChild(i);
                    if (getAttributeValue(currentMaterialElement, "alpha", false)) {
                        child.setQueueBucket(RenderQueue.Bucket.Transparent);
                        MaterialFactory.setTransparent(material, true);
                    }
                    child.setMaterial(material);
                }
            }
        }
    }

    private void tryLoadTexture(AssetManager assetManager, Material material, String materialParameter, String textureName, String sourceName) {
        if (textureName != null) {
            Texture texture = MaterialFactory.loadTexture(assetManager, getResourcesFilePath(sourceName) + textureName);
            material.setTexture(materialParameter, texture);
        }
    }

    private String getResourcesFilePath(String sourceName) {
        return "models/" + sourceName + "/resources/";
    }

    private void loadPosition(Node node) {
        if (positionElement != null) {
            Element locationElement = positionElement.getChild("location");
            if (locationElement != null) {
                float[] location = Util.parseToFloatArray(locationElement.getText().split(","));
                node.setLocalTranslation(location[0], location[1], location[2]);
            }
            Element directionElement = positionElement.getChild("direction");
            if (directionElement != null) {
                float[] direction = Util.parseToFloatArray(directionElement.getText().split(","));
                JMonkeyUtil.lookAtDirection(node, new Vector3f(direction[0], direction[1], direction[2]));
            }
            Element rotationElement = positionElement.getChild("rotation");
            if (rotationElement != null) {
                float[] rotation = Util.parseToFloatArray(rotationElement.getText().split(","));
                node.rotate(new Quaternion(rotation[0], rotation[1], rotation[2], rotation[3]));
            }
        }
    }

    private void loadModifiers() {
        modelModifiers.clear();
        if (modifiersElement != null){
            for (Object childObject : modifiersElement.getChildren("modifier")) {
                Element modifierElement = (Element) childObject;
                ModelModifier modelModifier = Util.createObjectByClassName("com.destroflyer.battlebuds.client.models.modifiers.ModelModifier_" + modifierElement.getText());
                modelModifiers.add(modelModifier);
            }
        }
    }

    public LinkedList<ModelModifier> getModelModifiers() {
        return modelModifiers;
    }

    private void applyGeometryInformation(Node node) {
        LinkedList<Geometry> geometryChilds = JMonkeyUtil.getAllGeometryChilds(node);
        for (Geometry geometry : geometryChilds) {
            Material material = geometry.getMaterial();
            MaterialFactory.generateAmbientColor(material, materialAmbient);
            material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        }
    }

    public String getName() {
        return name;
    }

    public Vector3f getModelScale() {
        return modelScale;
    }

    public String getRigType() {
        return rigType;
    }
}
