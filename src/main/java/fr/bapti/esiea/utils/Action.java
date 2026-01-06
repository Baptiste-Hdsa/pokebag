package fr.bapti.esiea.utils;

import fr.bapti.esiea.ui.battle.PlayBattleMulti;
import fr.bapti.esiea.ActionType;

public class Action {
    public ActionType type;
    public int index;

    public Action(ActionType t, int i) { type = t; index = i; }
}
