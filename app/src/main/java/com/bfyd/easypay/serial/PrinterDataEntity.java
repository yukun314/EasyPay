package com.bfyd.easypay.serial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bfyd.easypay.utils.HexDump;

import java.io.UnsupportedEncodingException;

/**
 * Created by zyk on 2016/8/4.
 */
public class PrinterDataEntity extends SerialEntity{

	//初始化命令 1B 40
	public boolean restoreDefault = false;

	public String result="";
	public Bitmap mBitmap;
	public byte [] bitmapdata;
	private boolean abc = false;
	private int star = 0;
	private int end = 0;
	public PrinterDataEntity(byte [] data){
		int length = data.length;
		System.out.println("data.length:"+data.length);
		StringBuffer buffer = new StringBuffer();
		for(int i = 0;i<length;i++){
			byte d = data[i];
			if(byteToInt(d) == 10){//换行
				a(i,data);
				result+="\n";
				star = i+1;
			}else if(byteToInt(d) == 27){//1b
				a(i,data);
				i++;
				d = data[i];
				if(byteToInt(d) == 64){
					restoreDefault = true;
				}else if(byteToInt(d) == 33){
					//设置字符打印方式
					i++;
					d = data[i];
					System.out.println("设置字符打印方式:"+d);
				}else if(byteToInt(d) == 74){
					//执行n/144英寸走纸
					i++;
					d = data[i];
					System.out.println("走纸:"+(byteToInt(d)/144.0f)+"英寸  d:"+byteToInt(d));
				}else if(byteToInt(d) == 105){
					System.out.println("1b 69 这是什么命令？");
				}else if(byteToInt(d) == 42){
					abc = true;
					int m = byteToInt(data[++i]);
					int n1 = byteToInt(data[++i]);
					int n2 = byteToInt(data[++i]);
					//1B   2A  m  n1  n2  [d]k
					//m=0，1，32，33。n1=0~255，n2=0~3。d=0~255。
					//k=N1+256xN2（m=0，1）
					//k=（N1+256xN2）x3（m=32，33）
					int k = n1+256*n2;
					if(m == 32 | m == 33){
						k = 3*k;
					}
					int dstPos = 0;
					if(bitmapdata == null){
						bitmapdata = new byte[k];
					}else{
						byte[]old = bitmapdata;
						dstPos = old.length;
						bitmapdata = new byte[dstPos+k];
					}
					System.arraycopy(data, i, bitmapdata, dstPos, k);

					byte [] aa = new byte[k];
					System.arraycopy(data, i, aa, 0, k);
					System.out.println(HexDump.dumpHexString(aa));
					System.out.println("i="+i);
//					i+=k;
					System.out.println("i="+i);

					System.out.println("m:"+m+" n1:"+n1+"  n2:"+n2);
				}
				star = i+1;
			}else if(byteToInt(d) == 28){//1c
				a(i,data);
				i++;
				d = data[i];
				if(byteToInt(d) == 33){
					i++;
					d = data[i];
					System.out.println("字体设置:"+byteToInt(d));
					//FIXME 汉字综合选择
				}
				star = i+1;
			}else if(byteToInt(d) == 29) {//1d
				a(i,data);
				i++;
				d = data[i];
				if(byteToInt(d) == 33){
					i++;
					d = data[i];
					System.out.println("不知到什么意思1d 21:"+byteToInt(d));

				}
				star = i+1;
			}
		}
	}

	private void a(int i,byte [] data){
		end = i;
//		result+=new String(data,star,end - star);
		try {
			result+=new String(data,star,end - star,"gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(bitmapdata != null && bitmapdata.length >0) {
			System.out.println("bitmapdata.length:"+bitmapdata.length);
			Bitmap bitmap = bytes2Bimap(bitmapdata);
			if (bitmap != null) {
				mBitmap = bitmap;
			}
		}
	}

	public int byteToInt(byte b) {
		//Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}

	private Bitmap bytes2Bimap(byte[] b) {
		if(abc) {
			if (b.length > 0) {
				return BitmapFactory.decodeByteArray(b, 0, b.length);
			}
			abc = false;
		}
		return null;
	}

}
