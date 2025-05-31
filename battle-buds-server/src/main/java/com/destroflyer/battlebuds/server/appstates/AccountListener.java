package com.destroflyer.battlebuds.server.appstates;

import com.destroflyer.battlebuds.shared.Account;

public interface AccountListener {

    void onLogin(Account account);

    void onLogout(Account account);
}
