package SBUGRAM;


import javafx.scene.image.Image;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Post extends Comment {

    private Image image;
    private final String title;
    private ConcurrentSkipListSet<String> like = new ConcurrentSkipListSet<>();
    private final Vector<String> repost = new Vector<>();
    private final Vector<Comment> comments = new Vector<>();

    public Post(String userName, String message, String title) {
        super(userName, message);
        this.title = title;
    }
/*
    public Post(String userName, String message, String title, Image image) {
        this(userName, message, title);
        this.image = image;
    }

    public Post(String userName, String message, String title, File image) {
        this(userName, message, title, new Image(image.toURI().toString()));
    }*/

    public Post Repost(String userName) {
        if (!repost.contains(userName)) {
            synchronized (this.repost) {
                repost.add(userName);
            }
        }
        return this;
    }

    public void UnRepost(String userName) {
        if (repost.contains(userName)) {
            synchronized (this.repost) {
                repost.remove(userName);
            }
        }
    }

    public void Comment(Comment comment) {
        synchronized (this.comments) {
            comments.add(comment);
        }
    }

    public void UnComment(Comment comment) {
        synchronized (this.comments) {
            comments.remove(comment);
        }
    }

    public Image getImage() {
        return image;
    }

    public List<Comment> getComments() {
        return this.comments.stream().sorted(Comment::compareTo).collect(Collectors.toList());
    }

    public String getTitle() {
        return title;
    }

    public ConcurrentSkipListSet<String> getLike() {
        return like;
    }

    public List<String> getRepost() {
        return repost;
    }

    public long getReposts() {
        synchronized (like) {
            return repost.size();
        }
    }

    public long getLikes() {
        synchronized (like) {
            return like.size();
        }
    }

    public void Like(String userName) {
        if (userName.equals(getUser())) return;
        synchronized (like) {
            this.like.add(userName);
        }
    }

    public void UnLike(String userName) {
        if (userName.equals(getUser())) return;
        synchronized (like) {
            this.like.remove(userName);
        }
    }

    private <T> void resizer(ArrayBlockingQueue<T> list) {
        synchronized (list) {
            ArrayBlockingQueue<T> resize = list;
            list = new ArrayBlockingQueue<T>(resize.size()*2);
            list.addAll(resize);
        }
    }



    @Override
    public int compareTo(Object object) {
        Post post = (Post) object;
        return this.getDate().compareTo(post.getDate());
    }

    @Override
    public String toString() {
        //Todo you should make it better name or username for example
        return getUser() + ":(" + title + ")\n\t" + getMessage();
    }

    public void check() {
        this.like.stream()
                .filter(userName -> userName.equals(getUser()))
                .forEach(userName -> like.remove(userName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Post post = (Post) o;
        return Objects.equals(title, post.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),title);
    }
}
