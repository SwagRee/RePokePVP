package io.github.swagree.pokecard.enums;

/**
 * 枚举类，枚举所有卡的名称和对应的中文名
 * 方便后续调用显示
 */
public enum EnumCardName {

    Mt("mt", "梦特"),
    Shiny("shiny", "闪光"),
    MaxLevel("maxLevel", "满级"),
    ClearLevel("clearLevel", "等级清空"),
    Gender("gender", "性别"),
    ClearEvs("clearEvs", "努力值清空"),
    MaxIvs("maxIvs", "满V"),
    AnyIvs("anyIvs",  "任意V"),
    AnyEvs("anyEvs", "任意努力值"),
    AnyGrowth("anyGrowth", "任意体型"),
    AnyNature("anyNature","任意性格"),
    rdNature("rdNature","随机性格"),
    Form("form","形态"),
    Texture("texTure", "材质"),
    Test("move", "技能"),
    PokeBall("pokeBall", "改球种"),
    Bind("bind", "绑定"),
    unBind("unbind", "解绑");

    private String cardName;
    private String cardNameCN;

    EnumCardName(String cardName, String cardNameCN) {
        this.cardName = cardName;
        this.cardNameCN = cardNameCN;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNameCN() {
        return cardNameCN;
    }

    public static String getValueByKey(String key) {
        for (EnumCardName enumCard : EnumCardName.values()) {
            if (enumCard.getCardName().equals(key)) {
                return enumCard.getCardNameCN();
            }
        }
        return null;  // 如果未找到匹配的键，则返回null或者抛出异常
    }


}

