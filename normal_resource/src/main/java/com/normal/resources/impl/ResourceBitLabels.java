package com.normal.resources.impl;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class ResourceBitLabels  implements TemplateSequenceModel {

    private static final int MAX_LENGTH = Integer.SIZE - 1;
    private BitSet value;

    // for display
    private List<ResourceLabelEnum> displayLabels;

    public ResourceBitLabels() {
        this.value = new BitSet();
    }

    public ResourceBitLabels(int value) {
        this.value = BitSet.valueOf(BigInteger.valueOf(value).toByteArray());
        displayLabels = this.getLabels();
    }

    public void setFlag(int flagIndex) {
        if (flagIndex > MAX_LENGTH) {
            throw new IndexOutOfBoundsException("max idx " + MAX_LENGTH);
        }
        value.set(flagIndex);
    }

    public boolean hasFlag(int flagIndex) {
        if (flagIndex > MAX_LENGTH) {
            throw new IndexOutOfBoundsException("max idx " + MAX_LENGTH);
        }
        return value.get(flagIndex);
    }

    public void clearFlag(int flagIndex) {
        if (flagIndex > MAX_LENGTH) {
            throw new IndexOutOfBoundsException("max idx " + MAX_LENGTH);
        }
        value.clear(flagIndex);
    }

    public void clear() {
        value.clear();
    }

    public int toInt() {
        int bitSize = value.size();
        int result = 0;
        for (int i = 0; i < bitSize; i++) {
            result += value.get(i) ? (1 << i) : 0;
        }
        return result;
    }

    @Override
    public String toString() {
        return value.toString();
    }


    public List<ResourceLabelEnum> getLabels() {
        List<ResourceLabelEnum> labels = new ArrayList<>();
        ResourceLabelEnum[] values = ResourceLabelEnum.values();
        for (int i = value.nextSetBit(0); i != -1; ) {
            if (i < values.length) {
                labels.add(values[i]);
            }
            i = value.nextSetBit(i + 1);
        }
        return labels;
    }


    @Override
    public TemplateModel get(int i) throws TemplateModelException {
        ResourceLabelEnum label = this.displayLabels.get(i);
        return new SimpleScalar(label.getValue());
    }

    @Override
    public int size() throws TemplateModelException {
        return this.displayLabels.size();
    }
}