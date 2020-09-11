package com.normal.model.shop;

import com.normal.model.BizDictEnums;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.PropertyVetoException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: fei.he
 */
@Data
public class GoodCat {

    private Vo self;

    private List<Vo> children = new ArrayList<>(1);


    public GoodCat(BizDictEnums self) {
        this.self = new Vo(self);
    }

    public void addChild(BizDictEnums child) {
        this.children.add(new Vo(child));
    }

    @Data
    public static class Vo{
        private String materialId;
        private String name;

        public Vo(BizDictEnums dictEnum) {
            this.materialId = dictEnum.key();
            this.name = dictEnum.value();
        }
    }
}
