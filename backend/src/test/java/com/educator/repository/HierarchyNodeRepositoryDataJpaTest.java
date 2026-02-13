package com.educator.repository;

import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HierarchyNodeRepositoryDataJpaTest {

    @Autowired
    private HierarchyNodeRepository repository;

    @Test
    void findAllVisiblePublished_returnsOnlyVisiblePublishedNonDeletedNodesOrdered() {
        saveNode("c", 3, true, true, false);
        saveNode("a", 1, true, true, false);
        saveNode("hidden", 2, false, true, false);
        saveNode("draft", 4, true, false, false);
        saveNode("deleted", 5, true, true, true);

        List<HierarchyNode> result = repository.findAllVisiblePublished();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(HierarchyNode::getSlug).containsExactly("a", "c");
    }

    private void saveNode(String slug, int sortOrder, boolean visible, boolean published, boolean deleted) {
        HierarchyNode node = new HierarchyNode();
        node.setSlug(slug);
        node.setNameEn(slug + "-name");
        node.setDescriptionEn("desc");
        node.setSortOrder(sortOrder);
        node.setVisible(visible);
        node.setPublished(published);
        node.setDeleted(deleted);
        node.setCreatedBy("test");
        repository.save(node);
    }
}


