package com.peterung.redditzen.data.db;


import com.peterung.redditzen.data.api.model.Account;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.db.entities.AccountEntity;
import com.peterung.redditzen.data.db.entities.PostEntity;

import org.threeten.bp.DateTimeUtils;

public class EntityConvert {

    public static PostEntity convert(Post post) {
        PostEntity postEntity = new PostEntity();
        postEntity.setApprovedBy(post.approvedBy);
        postEntity.setArchived(post.archived);
        postEntity.setAuthor(post.author);
        postEntity.setAuthorFlairText(post.authorFlairText);
        postEntity.setBannedBy(post.bannedBy);
        postEntity.setContestMode(post.contestMode);
        postEntity.setCreated(DateTimeUtils.toDate(post.created));
        postEntity.setDomain(post.domain);
        postEntity.setDowns(post.downs);
        postEntity.setGilded(post.gilded);
        postEntity.setHidden(post.hidden);
        postEntity.setHideScore(post.hideScore);
        postEntity.setId(post.id);
        postEntity.setIsSelf(post.isSelf);
        postEntity.setLikes(post.likes);
        postEntity.setLinkFlairText(post.linkFlairText);
        postEntity.setName(post.name);
        postEntity.setNumComments(post.numComments);
        postEntity.setOver18(post.over18);
        postEntity.setQuarantine(post.quarantine);
        postEntity.setScore(post.score);
        postEntity.setSaved(post.saved);
        postEntity.setSelftext(post.selftext);
        postEntity.setStickied(post.stickied);
        postEntity.setSubreddit(post.subreddit);
        postEntity.setSuggestedSort(post.suggestedSort);
        postEntity.setThumbnail(post.thumbnail);
        postEntity.setTitle(post.title);
        postEntity.setSubredditId(post.subredditId);
        postEntity.setUps(post.ups);
        postEntity.setUrl(post.url);
        postEntity.setVisited(post.visited);

        return postEntity;
    }


    public static AccountEntity convert(Account account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCommentKarma(account.commentKarma);
        accountEntity.setCreated(DateTimeUtils.toDate(account.created));
        accountEntity.setHasMail(account.hasMail);
        accountEntity.setHasModMail(account.hasModMail);
        accountEntity.setHasVerifiedEmail(account.hasVerifiedEmail);
        accountEntity.setId(account.id);
        accountEntity.setInboxCount(account.inboxCount);
        accountEntity.setHasVerifiedEmail(account.hasVerifiedEmail);
        accountEntity.setIsFriend(account.isFriend);
        accountEntity.setIsMod(account.isMod);
        accountEntity.setLinkKarma(account.linkKarma);
        accountEntity.setModhash(account.modhash);
        accountEntity.setOver18(account.over18);
        accountEntity.setName(account.name);

        return accountEntity;
    }
}
