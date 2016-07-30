package com.bfyd.easypay.serial;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zyk on 2016/7/29.
 * 把接收到的数据解析到相应的各个字段
 */
public class CustomerDisplayEntity {

	public static final int NONE = 0;//全暗
	public static final int UNITPRICE = 1;//单价亮
	public static final int TOTALPRICE = 2;//总计亮
	public static final int RECEIVABLES = 3;//收款
	public static final int CHANGE = 4;//找零

	@IntDef({NONE, UNITPRICE, TOTALPRICE, RECEIVABLES, CHANGE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface NumberType {
	}
	//1B 40 0C 1B 51 41 35 2E 30 30 0D 1B 73 31

	//客显上显示的数字(单价 总计 收款等的数值) 1B 51 41 35 2E 30 30 0D
	public float number;

	//初始化命令 1B 40 0C
	public boolean restoreDefault = false;

	//显示数字的类型 全暗 单价 总计等 1B 73 31
	@NumberType
	public int numberType = NONE;

	//FIXME 暂时先解析上面三个变量
	//清屏命令


	public CustomerDisplayEntity(byte [] data){
		//27  64  12  27  81  65  56  46  48  48  13  27  115  49
		//1B  40  0C  1B  51  41  35  2E  30  30  0D  1B  73   31
		int length = data.length;
		for(int i = 0;i<length;i++){
			byte d = data[i];
			if(d == 27){//1b
				i++;
				d = data[i];
				if(d == 64){
					//1b 40 0c
					restoreDefault = true;
				}else if(d == 115) {
					i++;
					d = data[i];
					if(d == 48){
						numberType = NONE;
					}else if(d == 49){
						numberType = UNITPRICE;
					}else if(d == 50){
						numberType = TOTALPRICE;
					}else if(d == 51){
						numberType = RECEIVABLES;
					}else if(d == 52){
						numberType = CHANGE;
					}
				}else if(d == 81){
					i++;
					d = data[i];
					if(d == 65){
						i++;
						d = data[i];
						String numberStr = "";
						while(d != 13){
							numberStr += byteToStr(d);
							i++;
							d = data[i];
						}
						number = Float.parseFloat(numberStr);
					}
				}
			}
		}
	}

	private String byteToStr(byte b){
		String result = "";
		if(b>=48 && b <= 57){
			result = (b - 48)+"";
		}
		if(b == 46){
			result = ".";
		}
		return result;
	}

	@Override
	public String toString() {
		return "CustomerDisplayEntity{" +
				"number=" + number +
				", restoreDefault=" + restoreDefault +
				", numberType=" + numberType +
				'}';
	}
}
