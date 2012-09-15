/*
 * Created on 2004-5-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.gftech.util;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *  定义自已的ResultSet数据集,用来贮存查询数据库返回的记录集.
 *  实现自定义的记录集的目的是可以尽早的关闭ResultSet和Statement，
 *  以防止因为疏忽而忘关闭导致的游标越界。
 * @author sinboy
 * 
 *
 */
public class GFResultSet {
	private int currentRow;//当前行数
	private int cols;//列数
	private int rows;//行数
	private ArrayList<HashMap> list;//存贮记录集
	private ArrayList<ArrayList> list2;//存贮记录集。为了给客户端程序提供通过序号得到列值的方法
	private ArrayList<String> valueList;//按顺序存贮一行的列值，以便客户程序可以通过序号而不是列名来得到列值
	
	/**
	 * 构造子。
	 * 把标准的ResultSet记录集转成自定义的记录集
	 * @param rs 标准的记录集
	 */
	public GFResultSet(ResultSet rs){
	   currentRow=-1;
	   list=new ArrayList<HashMap>(0);
	   list2=new ArrayList<ArrayList>(0);
	  
	   
	   if(rs!=null)
	      convert(rs);
	}
	
	private void convert(ResultSet rs){
		HashMap<String,String> oneRow;
		String colName;
		String colValue;
		
		if(rs!=null){
			try{
			/**
			 * 得到行数和列数
			 */
			ResultSetMetaData rsMeta=rs.getMetaData();
			cols=rsMeta.getColumnCount();
			rs.last();
			rows=rs.getRow();
			
			//把行数指针移动第一行的前面
            rs.beforeFirst();
			while(rs.next()){
				oneRow=new HashMap<String,String>(0);
		        valueList=new ArrayList<String>(0);
				for(int i=1;i<=cols;i++){
				  colName=rsMeta.getColumnName(i);
				  colValue=rs.getString(i);
				  oneRow.put(colName.toUpperCase() ,colValue );
				  valueList.add(colValue);
				}
				list.add(oneRow);
				list2.add(valueList);
			}
			
			
			}catch(SQLException e){
				e.printStackTrace() ;
			}
			
		}
	}
	
	/**
	 * 判断记录集中是否还有下一行记录
	 * @return  如果有，返回TRUE
	 */
	public boolean next(){
		if(currentRow>=rows-1)
		  return false;
		
		currentRow++;
		return true;
	}
	
	/**
	 * 根据列名，返回此列名对应的列值
	 * @param colName 列名
	 * @return 列值
	 */
	public String getString(String colName){
		HashMap oneRow=null;
		if(colName!=null){
			if(list!=null){
			
			oneRow=(HashMap)list.get(currentRow);
			return (String)oneRow.get(colName.toUpperCase() );
			}
		}
		return null;
	}
	 
	
	/**
	 * 提供按序号来访问列值的方法。
	 * @param col 列的序号
	 * @return 对应的列值
	 */
	/*
	 * 本来想直接通过list来实现，但是
	 * 目前存在的问题是由HashMap.value()得到VALUES的COLLECTION之后，
	 * 通过ITERATOR来访问时，发现值的次序不是按原有的顺序排列，所以
	 * 无法用此方法来实现按序号访问。源程序如下：
	 *  HashMap oneRow=null;
	 *  Collection ct=null;
	 * Iterator itr=null;
	 * String[] names=null;
	 * String colName=null;
	 * int index=-1;
	 * if(col>=0){
	 *    if(list!=null){
	 *     oneRow=(HashMap)list.get(currentRow);
	 *     ct=oneRow.values();
	 *     itr=ct.iterator();
	 *      while(itr.hasNext() ){
	 *         colName=(String)itr.next()  ;
	 *         index++;
	 *         if(col==index)
	 *           return colName;
	 *     }
	 *   }
	 * }
	 * return null;
	 * 
	 * 
	 * 解决的办法把每一行的列值按顺序放到ArrayList中，避开MAP。
	 */
	public String getString(int col){
		ArrayList alRow=null;
		 
		if(col>=0){
			if(list2!=null){
			   alRow=(ArrayList)list2.get(currentRow);
			   return (String)alRow.get(col);
			}
		}

		return null;
	}
   
   public void beforeFirst(){
   	 currentRow=-1;
   }
   
   /**
    * 返回记录集的列数
    * @return
    */
   public int cols(){
   	  return cols;
   }
   
   /**
    * 返回记录集的行数
    * @return
    */
   public int rows(){
   	return rows;
   }
}
