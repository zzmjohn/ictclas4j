package org.ictclas4j.remote;

/**
 * <pre>
 * 命令类型
 * 1：分词处理请求
 * -1：分词处理回应
 * 2：创建索引请求
 * -2：创建索引回应
 * 3：删除索引请求
 * -3：删除索引回应
 * 4：查询索引结果请求
 * -4：查询索引结果回应
 * </pre>
 * 
 * @author sinboy
 * @since 2007.6.28
 */
public interface CmdID {
	byte SEG_REQUEST = 1;

	byte SEG_RESPONSE = -1;

	byte CREATE_INDEX_REQUEST = 2;

	byte CREATE_INDEX_RESPONSE = -2;

	byte DEL_INDEX_REQUEST = 3;

	byte DEL_INDEX_RESPONSE = -3;

	byte QUERY_INDEX_REQUEST = 4;

	byte QUERY_INDEX_RESPONSE = -4;

	byte BATCH_DEL_INDEX_REQUEST = 5;

	byte BATCH_DEL_INDEX_RESPONSE = -5;

	byte UPDATE_CORPFLAG_REQUEST = 6;

	byte UPDATE_CORPFLAG_RESPONSE = -6;
	
	byte BATCH_CREATE_INDEX_REQUEST=7;
	
	byte BATCH_CREATE_INDEX_RESPONSE=-7;
	
	//原生分词请求，即生成的分词结果为SegAtom的表示形式，而不是最终的字符串表示结果
	byte RAW_SEG_REQUEST = 8;

	byte RAW_SEG_RESPONSE = -8;
}
