package SBUGRAM;

import java.io.Serializable;
import java.util.stream.Collectors;

public class RePost implements Serializable {
    public Post post;
    public RePost(Post post) {
        this.post = post;
    }

    public Post getPost(Server server) {
        return server.allUsers.get(this.post.getUser()).posts.stream()
                .filter(post -> post.equals(this.post))
                .collect(Collectors.toList()).get(0);
    }

    public Post getInnerPost() {
        return this.post;
    }
}
