package com.pascalhain.runconquer.data.source;

import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.model.User;

public interface UserDataSource {
    User getCurrentUser();
    Team getCurrentTeam();
}
