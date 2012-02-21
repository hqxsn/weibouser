package com.julu.weibouser.processing.indexing;

import com.julu.weibouser.config.Configuration;
import com.julu.weibouser.logger.ConsoleLogger;
import com.julu.weibouser.model.User;
import com.julu.weibouser.processing.Processing;
import com.julu.weibouser.processing.UserProcessingEvent;
import com.julu.weibouser.processing.UserStreamUtil;
import com.julu.weibouser.processing.states.StatesMachine;
import com.julu.weibouser.util.Utils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TwinsFather.
 * User: andy.song
 * Date: 2/20/12
 * Time: 11:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Indexer implements Processing {
    private static ConsoleLogger logger = new ConsoleLogger(Indexer.class.getName());

    public boolean processing(UserProcessingEvent event) {

        boolean success = new LuceneIndexer(event).invoke();
        if (!success) {
            //event.setCurrentState(StatesMachine.getPreviousState(event.getCurrentState()));
            event.increaseRetryCount();
            return false;
        } else {
            event.setCurrentState(StatesMachine.getNextState(event.getCurrentState()));
            event.resetRetryCount();
        }

        return true;
    }

    private String getIndexDirectory() {
        return System.getProperty(Configuration.PROCESSING_FILES_DIRECTORY) + 
                System.getProperty("file.separator") + System.getProperty(Configuration.INDEX_FILES_DIRECTORY);
    }


    private class LuceneIndexer {
        private UserProcessingEvent event;

        public LuceneIndexer(UserProcessingEvent event) {
            this.event = event;
        }

        public boolean invoke() {
            File file = null;
            IndexWriter writer = null;
            Directory dir = null;
            try {
                List<User> users = UserStreamUtil.deserialization(event.getRelatedValue());
                file = new File(getIndexDirectory());
                dir = FSDirectory.open(file);

                Analyzer analyzer = new StandardAnalyzer( Version.LUCENE_35 );
                IndexWriterConfig iwc = new IndexWriterConfig( Version.LUCENE_35,
                        analyzer );
                iwc.setOpenMode( IndexWriterConfig.OpenMode.CREATE_OR_APPEND );
                writer = new IndexWriter( dir, iwc );

                List<Document> documents = new ArrayList<Document>();
                for(User user:users) {
                    Document document = new Document();
                    //TODO add need index fields
                    document.add(new Field("uid", String.valueOf(user.getUid()), Field.Store.YES, Field.Index.ANALYZED));
                    document.add(new Field("originalSourceUid", String.valueOf(user.getOriginalSourceUid()), Field.Store.YES, Field.Index.ANALYZED));
                    document.add(new Field("path", event.getRelatedFilePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                    documents.add(document);
                }

                writer.addDocuments(documents);
                writer.commit();


            } catch (IOException e) {
                //TODO add logic later
                logger.logError("Index meet exception", e);
                return false;
            } finally {

                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dir != null) {
                    try {
                        dir.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return true;
        }
    }
}
