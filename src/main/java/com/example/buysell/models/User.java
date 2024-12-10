package com.example.buysell.models;

import com.example.buysell.models.enums.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "numberPhone")
    private String numberPhone;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatarImage;
    @Column(name = "password", length = 1000)
    private String password;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Product> products = new ArrayList<>();
    private LocalDateTime dateOfCreate;

    @OneToMany(mappedBy = "user")
    private List<Delivery> deliveries;

    @PrePersist
    private void init() {
        dateOfCreate = LocalDateTime.now();
    }

    public void addProductToUser(Product product) {
        product.setUser(this);
        products.add(product);
    }

// security

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> role.name())
                .collect(Collectors.toList());
    }




    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNumberPhone() {
        return this.numberPhone;
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public Image getAvatarImage() {
        return this.avatarImage;
    }

    public String getPassword() {
        return this.password;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public LocalDateTime getDateOfCreate() {
        return this.dateOfCreate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAvatarImage(Image avatarImage) {
        this.avatarImage = avatarImage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setDateOfCreate(LocalDateTime dateOfCreate) {
        this.dateOfCreate = dateOfCreate;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", email=" + this.getEmail() + ", numberPhone=" + this.getNumberPhone() + ", name=" + this.getName() + ", active=" + this.isActive() + ", avatarImage=" + this.getAvatarImage() + ", password=" + this.getPassword() + ", roles=" + this.getRoles() + ", products=" + this.getProducts() + ", dateOfCreate=" + this.getDateOfCreate() + ")";
    }
}
