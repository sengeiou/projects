package com.normal.openapi.impl;


/**
 * @author: fei.he
 */
public interface ParamConvertor<MR, OR, OB, MB> {

    OR toOpenReq(MR myReqParam);

    MB toMyRes(OB openBackParam);
}
