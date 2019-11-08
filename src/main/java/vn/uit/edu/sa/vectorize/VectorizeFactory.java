package vn.uit.edu.sa.vectorize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

public class VectorizeFactory {
	private String filePath = "";
	private SentenceIterator iter = null;
	public VectorizeFactory() {
		
	}
	
	public VectorizeFactory(String _filePath) {
        //this.filePath = new ClassPathResource(_filePath).getFile().getAbsolutePath();
		iter = new LineSentenceIterator(new File(_filePath));
		//SentenceIterator iter = new BasicLineIterator(filePath);
	}

	public void run() {
		try {
	        TokenizerFactory t = new DefaultTokenizerFactory();
	        t.setTokenPreProcessor(new CommonPreprocessor());
	        
	        Word2Vec vec = new Word2Vec.Builder()
	                .minWordFrequency(5)
	                .layerSize(300)
	                .seed(42)
	                .windowSize(5)
	                .iterate(iter)
	                .tokenizerFactory(t)
	                .build();
	        
	        vec.fit();
	        
	        
	        WordVectorSerializer.writeWordVectors(vec, System.getProperty("user.dir") + "/word2vecModel/vector.txt");
	        //WordVectorSerializer.writeWord2VecModel(vec, System.getProperty("user.dir") + "/word2vecModel/vector.txt");
	        	        
			/*
			 * System.out.println(vec.similarWordsInVocabTo("đại_học", 0.7));
			 * 
			 * double cosSim = vec.similarity("giảng_viên", "đại_học");
			 * System.out.println(cosSim);
			 * 
			 * Collection<String> lst1 = vec.wordsNearest("giảng_viên", 10);
			 * System.out.println(lst1);
			 * 
			 * Collection<String> lst2 = vec.wordsNearest("tư_vấn", 10);
			 * System.out.println(lst2);
			 */
	        
	        System.out.println("Finish vectorized!");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
