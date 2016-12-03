package com.peterung.redditzen.ui.profile;


import com.peterung.redditzen.data.db.entities.AccountEntity;

public interface ProfileContract {
    interface View {
        void showProfile(AccountEntity accountEntity);
    }

    interface UserActionsListener {
        void loadProfile();
        void initialize(View view);
        void release();
    }
}
