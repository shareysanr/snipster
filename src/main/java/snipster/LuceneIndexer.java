package snipster;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneIndexer {
    private Directory indexDirectory;
    private IndexWriter indexWriter;

    public LuceneIndexer(String indexPath) throws IOException {
        indexDirectory = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        indexWriter = new IndexWriter(indexDirectory, config);
    }

    public void indexSnippet(String title, String code, String tags) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, TextField.Store.YES));
        doc.add(new TextField("code", code, TextField.Store.YES));
        doc.add(new StringField("tags", tags, StringField.Store.YES));
        indexWriter.addDocument(doc);
    }

    public void searchSnippets(String queryStr) throws Exception {
        DirectoryReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);
        StoredFields storedFields = searcher.storedFields();

        Query query = new QueryParser("code", new StandardAnalyzer()).parse(queryStr);
        TopDocs results = searcher.search(query, 10);

        for (ScoreDoc hit : results.scoreDocs) {
            Document doc = storedFields.document(hit.doc);
            System.out.println("Title is: " + doc.get("title"));
        }

        reader.close();
    }

    public void close() throws IOException {
        indexWriter.close();
    } 
}
