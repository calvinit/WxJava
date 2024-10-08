package com.github.binarywang.wxpay.bean.ecommerce;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * add by 306932545@qq.com
 * 请求补差回退API-请求对象
 * <pre>
 *   https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter7_5_2.shtml
 * </pre>
 */
@Data
@NoArgsConstructor
public class SubsidiesReturnRequest implements Serializable {

  /**
   * <pre>
   * 字段名：二级商户号
   * 变量名：sub_mchid
   * 是否必填：是
   * 类型：string（32）
   * 描述：
   *  补差的电商平台二级商户，填写微信支付分配的商户号。
   * 示例值：1900000109
   * </pre>
   */
  @SerializedName(value = "sub_mchid")
  private String subMchid;

  /**
   * <pre>
   * 字段名：微信订单号
   * 变量名：transaction_id
   * 是否必填：是
   * 类型：string（64）
   * 描述：
   *  微信支付订单号。
   * 示例值： 4208450740201411110007820472
   * </pre>
   */
  @SerializedName(value = "transaction_id")
  private String transactionId;

  /**
   * <pre>
   * 字段名：商户补差回退单号
   * 变量名：out_order_no
   * 是否必填：是
   * 类型：string（64）
   * 描述：
   * 原发起补差请求时使用的商户系统内部的补差单号。
   * 示例值：P20150806125346
   * </pre>
   */
  @SerializedName(value = "out_order_no")
  private String outOrderNo;

  /**
   * <pre>
   * 字段名：补差金额
   * 变量名：amount
   * 是否必填：是
   * 类型：int64
   * 描述：
   * 补差金额，单位为分，只能为整数，不能超过下单时候的最大补差金额。
   * 注意：单笔订单最高补差金额为10000元
   * 示例值：10
   * </pre>
   */
  @SerializedName(value = "amount")
  private Integer amount;

  /**
   * <pre>
   * 字段名：补差描述
   * 变量名：description
   * 是否必填：是
   * 类型：string（80）
   * 描述：
   * 补差备注描述，查询的时候原样带回。
   * 示例值：测试备注
   * </pre>
   */
  @SerializedName(value = "description")
  private String description;

  /**
   * <pre>
   * 字段名：微信退款单号
   * 变量名：refund_id
   * 是否必填：否
   * 类型：string（64）
   * 描述：
   * 微信退款单号，微信支付系统退款返回的唯一标识。
   * 用户零钱账户异常，无法在线发起退款时，此字段可以不传；其他情况下必传。
   * 示例值：3008450740201411110007820472
   * </pre>
   */
  @SerializedName(value = "refund_id")
  private String refundId;
}
