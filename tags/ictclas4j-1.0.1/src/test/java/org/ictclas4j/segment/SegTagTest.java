package org.ictclas4j.segment;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

public class SegTagTest
{
	/**
	 * Tokenizing this text causes an endless loop: 月份牌
	 * @see <a href="http://code.google.com/p/ictclas4j/issues/detail?id=13">Issue 13</a>
	 */
	@Test(expected=IllegalStateException.class)
	public void testEndlessLoop() throws Exception
	{
		final InputStream coreDictIn = new FileInputStream("data/coreDict.dct");
		final InputStream bigramDictIn = new FileInputStream("data/BigramDict.dct");
		final InputStream personTaggerDctIn = new FileInputStream("data/nr.dct");
		final InputStream personTaggerCtxIn = new FileInputStream("data/nr.ctx");
		final InputStream transPersonTaggerDctIn = new FileInputStream("data/tr.dct");
		final InputStream transPersonTaggerCtxIn = new FileInputStream("data/tr.ctx");
		final InputStream placeTaggerDctIn = new FileInputStream("data/ns.dct");
		final InputStream placeTaggerCtxIn = new FileInputStream("data/ns.ctx");
		final InputStream lexTaggerCtxIn = new FileInputStream("data/lexical.ctx");
		SegTag seg = new SegTag(1, coreDictIn, bigramDictIn, personTaggerDctIn, personTaggerCtxIn,
				transPersonTaggerDctIn, transPersonTaggerCtxIn, placeTaggerDctIn, placeTaggerCtxIn,
				lexTaggerCtxIn);
		seg.split("月份牌");
	}
}
