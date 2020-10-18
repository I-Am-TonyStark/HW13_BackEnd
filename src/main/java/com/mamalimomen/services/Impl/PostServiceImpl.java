package com.mamalimomen.services.Impl;

import com.mamalimomen.base.services.impl.BaseServiceImpl;
import com.mamalimomen.domains.Account;
import com.mamalimomen.domains.Post;
import com.mamalimomen.dtos.PostDTO;
import com.mamalimomen.dtos.PostSearchDTO;
import com.mamalimomen.repositories.PostRepository;
import com.mamalimomen.repositories.impl.PostRepositoryImpl;
import com.mamalimomen.services.PostService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

public class PostServiceImpl extends BaseServiceImpl<Long, Post, PostSearchDTO, PostRepository> implements PostService {
    public PostServiceImpl(EntityManager em) {
        super(new PostRepositoryImpl(em));
    }

    private final Function<Post, PostDTO> postChanger = Post::copyMeTo;

    @Override
    public boolean createNewPost(PostDTO dto) {
        Post post = new Post();

        return saveOne(post.copyMeFrom(dto));
    }

    @Override
    public List<PostDTO> retrieveManyExistPosts(PostDTO dto) {
        return repository.findManyPostsByAccountUsername(dto.getAccount().getUser().getUsername(), postChanger);
    }

    @Override
    public List<PostDTO> retrieveAllExistPosts() {
        return repository.findAllPosts(postChanger);
    }

    @Override
    public boolean updateExistPost(PostDTO dto) {
        Post post  = findOneById(Post.class,dto.getId()).get();

        return updateOne(post.copyMeFrom(dto));
    }

    @Override
    public boolean deleteExistPost(PostDTO dto) {
        Post post = findOneById(Post.class,dto.getId()).get();

        return deleteOne(post);
    }

    @Override
    public List<PostDTO> postAdvancedSearch() {
        return null;
    }
}
