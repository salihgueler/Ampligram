package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Photo type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Photos", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Photo implements Model {
  public static final QueryField ID = field("Photo", "id");
  public static final QueryField PHOTO_KEY = field("Photo", "photoKey");
  public static final QueryField DESCRIPTION = field("Photo", "description");
  public static final QueryField USERNAME = field("Photo", "username");
  public static final QueryField LOCATION = field("Photo", "location");
  public static final QueryField IS_FAVORITE = field("Photo", "isFavorite");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String photoKey;
  private final @ModelField(targetType="String", isRequired = true) String description;
  private final @ModelField(targetType="String", isRequired = true) String username;
  private final @ModelField(targetType="String", isRequired = true) String location;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean isFavorite;
  private final @ModelField(targetType="Comment") @HasMany(associatedWith = "photoID", type = Comment.class) List<Comment> comments = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getPhotoKey() {
      return photoKey;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getUsername() {
      return username;
  }
  
  public String getLocation() {
      return location;
  }
  
  public Boolean getIsFavorite() {
      return isFavorite;
  }
  
  public List<Comment> getComments() {
      return comments;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Photo(String id, String photoKey, String description, String username, String location, Boolean isFavorite) {
    this.id = id;
    this.photoKey = photoKey;
    this.description = description;
    this.username = username;
    this.location = location;
    this.isFavorite = isFavorite;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Photo photo = (Photo) obj;
      return ObjectsCompat.equals(getId(), photo.getId()) &&
              ObjectsCompat.equals(getPhotoKey(), photo.getPhotoKey()) &&
              ObjectsCompat.equals(getDescription(), photo.getDescription()) &&
              ObjectsCompat.equals(getUsername(), photo.getUsername()) &&
              ObjectsCompat.equals(getLocation(), photo.getLocation()) &&
              ObjectsCompat.equals(getIsFavorite(), photo.getIsFavorite()) &&
              ObjectsCompat.equals(getCreatedAt(), photo.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), photo.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getPhotoKey())
      .append(getDescription())
      .append(getUsername())
      .append(getLocation())
      .append(getIsFavorite())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Photo {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("photoKey=" + String.valueOf(getPhotoKey()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("username=" + String.valueOf(getUsername()) + ", ")
      .append("location=" + String.valueOf(getLocation()) + ", ")
      .append("isFavorite=" + String.valueOf(getIsFavorite()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static PhotoKeyStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Photo justId(String id) {
    return new Photo(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      photoKey,
      description,
      username,
      location,
      isFavorite);
  }
  public interface PhotoKeyStep {
    DescriptionStep photoKey(String photoKey);
  }
  

  public interface DescriptionStep {
    UsernameStep description(String description);
  }
  

  public interface UsernameStep {
    LocationStep username(String username);
  }
  

  public interface LocationStep {
    IsFavoriteStep location(String location);
  }
  

  public interface IsFavoriteStep {
    BuildStep isFavorite(Boolean isFavorite);
  }
  

  public interface BuildStep {
    Photo build();
    BuildStep id(String id);
  }
  

  public static class Builder implements PhotoKeyStep, DescriptionStep, UsernameStep, LocationStep, IsFavoriteStep, BuildStep {
    private String id;
    private String photoKey;
    private String description;
    private String username;
    private String location;
    private Boolean isFavorite;
    @Override
     public Photo build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Photo(
          id,
          photoKey,
          description,
          username,
          location,
          isFavorite);
    }
    
    @Override
     public DescriptionStep photoKey(String photoKey) {
        Objects.requireNonNull(photoKey);
        this.photoKey = photoKey;
        return this;
    }
    
    @Override
     public UsernameStep description(String description) {
        Objects.requireNonNull(description);
        this.description = description;
        return this;
    }
    
    @Override
     public LocationStep username(String username) {
        Objects.requireNonNull(username);
        this.username = username;
        return this;
    }
    
    @Override
     public IsFavoriteStep location(String location) {
        Objects.requireNonNull(location);
        this.location = location;
        return this;
    }
    
    @Override
     public BuildStep isFavorite(Boolean isFavorite) {
        Objects.requireNonNull(isFavorite);
        this.isFavorite = isFavorite;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String photoKey, String description, String username, String location, Boolean isFavorite) {
      super.id(id);
      super.photoKey(photoKey)
        .description(description)
        .username(username)
        .location(location)
        .isFavorite(isFavorite);
    }
    
    @Override
     public CopyOfBuilder photoKey(String photoKey) {
      return (CopyOfBuilder) super.photoKey(photoKey);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
    
    @Override
     public CopyOfBuilder location(String location) {
      return (CopyOfBuilder) super.location(location);
    }
    
    @Override
     public CopyOfBuilder isFavorite(Boolean isFavorite) {
      return (CopyOfBuilder) super.isFavorite(isFavorite);
    }
  }
  
}
