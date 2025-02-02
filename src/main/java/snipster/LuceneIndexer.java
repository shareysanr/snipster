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
import org.apache.lucene.index.Term;

import java.util.ArrayList;
import java.util.List;

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

    public void indexSnippet(int id, String title, String code, String tags) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(id), StringField.Store.YES));
        doc.add(new TextField("title", title, TextField.Store.YES));
        doc.add(new TextField("code", code, TextField.Store.YES));
        doc.add(new TextField("tags", tags, TextField.Store.YES));
        indexWriter.addDocument(doc);

        close();
    }

    // public void searchSnippets(String queryStr) throws Exception {
    //     DirectoryReader reader = DirectoryReader.open(indexDirectory);
    //     IndexSearcher searcher = new IndexSearcher(reader);
    //     StoredFields storedFields = searcher.storedFields();

    //     Query query = new QueryParser("code", new StandardAnalyzer()).parse(queryStr);
    //     TopDocs results = searcher.search(query, 10);

    //     for (ScoreDoc hit : results.scoreDocs) {
    //         Document doc = storedFields.document(hit.doc);
    //         System.out.println("Title is: " + doc.get("title"));
    //     }

    //     reader.close();
    // }

    public List<Snippet> searchSnippets(String queryStr) throws Exception {
        List<Snippet> searchResults = new ArrayList<>();
        DirectoryReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);
        StoredFields storedFields = searcher.storedFields();

        Query query = new QueryParser("code", new StandardAnalyzer()).parse(queryStr);
        TopDocs results = searcher.search(query, 10);

        for (ScoreDoc hit : results.scoreDocs) {
            Document doc = storedFields.document(hit.doc);
            String idString = doc.get("id");
            int id = Integer.parseInt(idString);
            String title = doc.get("title");
            String code = doc.get("code");
            String tags = doc.get("tags");
            //System.out.println("Title is: " + doc.get("title"));
            searchResults.add(new Snippet(id, title, code, tags));
        }

        reader.close();
        return searchResults;
    }

    public void updateIndex(int id, String title, String code, String tags) throws IOException {
        indexWriter.deleteDocuments(new Term("id", String.valueOf(id)));

        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(id), StringField.Store.YES));
        doc.add(new TextField("title", title, TextField.Store.YES));
        doc.add(new TextField("code", code, TextField.Store.YES));
        doc.add(new TextField("tags", tags, TextField.Store.YES));
        indexWriter.addDocument(doc);

        close();
    }

    public void deleteIndex(int id) throws IOException {
        indexWriter.deleteDocuments(new Term("id", String.valueOf(id)));

        close();
    }

    public void printAllSnippets() throws IOException {
        DirectoryReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);
        StoredFields storedFields = searcher.storedFields();

        System.out.println("All Snippets:");

        for (int i = 0; i < reader.maxDoc(); i++) {
            Document doc = storedFields.document(i);
            String id = doc.get("id");
            String title = doc.get("title");
            String code = doc.get("code");
            String tags = doc.get("tags");

            System.out.println("ID: " + id);
            System.out.println("Title: " + title);
            System.out.println("Code: " + code);
            System.out.println("Tags: " + tags);
            System.out.println("------------");
        }

        reader.close();
    }

    public void clearIndex() throws IOException {
        indexWriter.deleteAll();
        System.out.println("Cleared all indexed snippets");
        close();
    }

    public void close() throws IOException {
        indexWriter.close();
    } 
}
