package com.pascalhain.runconquer.data.source;

import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Territory;

import java.util.List;

public interface TerritoryDataSource {
    List<Territory> getTerritories();
    void claimTerritoryForRun(Run run);
}
