package com.pes.gkl.gate.Fragments.dummy;

import com.pes.gkl.gate.modelclasses.TestStat;
import com.pes.gkl.gate.modelclasses.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyStatContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<TestStat> ITEMS = new ArrayList<TestStat>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    static int i=1;
    public static Map<Integer, TestStat> ITEM_MAP = new HashMap<Integer, TestStat>();

    static {
        // Add 3 sample items.
        addItem(new TestStat("Please Wait",1, 2, 3, 4, 5));
        //addItem(new Topic("2", "Item 2"));
        //addItem(new Topic("3", "Item 3"));
    }

    private static void addItem(TestStat item) {
        ITEMS.add(item);
        ITEM_MAP.put(i, item);
        i+=1;
    }

    /**
     * A dummy item representing a piece of content.
     */
    /*
    public static class Topic {
        public String id;
        public String content;

        public Topic(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }*/
}
