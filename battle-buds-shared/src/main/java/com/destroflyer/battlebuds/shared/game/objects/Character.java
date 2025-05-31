package com.destroflyer.battlebuds.shared.game.objects;

import com.destroflyer.battlebuds.shared.network.BitInputStream;
import com.destroflyer.battlebuds.shared.network.BitOutputStream;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class Character extends PhysicsObject {

    @Getter
    @Setter
    protected Float baseMaximumHealth;
    @Getter
    @Setter
    protected Float currentHealth;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        checkHealth();
    }

    private void checkHealth() {
        if (currentHealth != null) {
            if (currentHealth <= 0) {
                onDeath();
            } else {
                float maximumHealth = getMaximumHealth();
                if (currentHealth > maximumHealth) {
                    currentHealth = maximumHealth;
                }
            }
            if (!isAlive()) {
                requestRemoveFromBoard();
            }
        }
    }

    protected void onDeath() {

    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void takeDamage(float damage) {
        takeDamage(damage, damage);
    }

    public void takeDamage(float preMitigationDamage, float postMitigationDamage) {
        currentHealth -= postMitigationDamage;
    }

    public void heal(float health) {
        if (currentHealth != null) {
            currentHealth += health;
        }
    }

    public Float getMaximumHealth() {
        return baseMaximumHealth;
    }

    @Override
    public void writeForClient(BitOutputStream outputStream) throws IOException {
        super.writeForClient(outputStream);
        outputStream.writeFloat_Unprecise_Nullable(currentHealth);
    }

    @Override
    public void readForClient(BitInputStream inputStream) throws IOException {
        super.readForClient(inputStream);
        currentHealth = inputStream.readFloat_Unprecise_Nullable();
    }
}
