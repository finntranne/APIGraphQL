package vn.iotstar.Controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import jakarta.transaction.Transactional;
import vn.iotstar.dto.*;
import vn.iotstar.entity.*;
import vn.iotstar.repository.*;

import java.util.List;

@Controller
public class GraphQLController {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public GraphQLController(UserRepository userRepo,
                             ProductRepository productRepo,
                             CategoryRepository categoryRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    // Queries
    @QueryMapping
    public List<Product> productsSortedByPriceAsc() {
        return productRepo.findAllByOrderByPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepo.findByCategoryId(categoryId);
    }

    @QueryMapping
    public List<User> users() { return userRepo.findAll(); }

    @QueryMapping
    public List<Category> categories() { return categoryRepo.findAll(); }

    // Mutations
    @MutationMapping
    public User createUser(@Argument CreateUserInput input) {
        User u = new User();
        u.setFullname(input.getFullname());
        u.setEmail(input.getEmail());
        u.setPassword(input.getPassword());
        u.setPhone(input.getPhone());
        return userRepo.save(u);
    }

    @MutationMapping
    public Category createCategory(@Argument CreateCategoryInput input) {
        Category c = new Category();
        c.setName(input.getName());
        c.setImages(input.getImages());
        return categoryRepo.save(c);
    }

    @MutationMapping
    public Product createProduct(@Argument CreateProductInput input) {
        Product p = new Product();
        p.setTitle(input.getTitle());
        p.setQuantity(input.getQuantity());
        p.setDesc(input.getDesc());
        p.setPrice(input.getPrice());

        userRepo.findById(input.getUserId()).ifPresent(p::setUser);

        if (input.getCategoryIds() != null) {
            input.getCategoryIds().forEach(cid ->
                categoryRepo.findById(cid).ifPresent(p.getCategories()::add)
            );
        }
        return productRepo.save(p);
    }
    
    @MutationMapping
    @Transactional
    public User updateUser(@Argument Long id, @Argument CreateUserInput input) {
        return userRepo.findById(id).map(u -> {
            u.setFullname(input.getFullname());
            u.setEmail(input.getEmail());
            u.setPassword(input.getPassword());
            u.setPhone(input.getPhone());
            return userRepo.save(u);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @MutationMapping
    @Transactional
    public Boolean deleteUser(@Argument Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // Category update/delete
    @MutationMapping
    @Transactional
    public Category updateCategory(@Argument Long id, @Argument CreateCategoryInput input) {
        return categoryRepo.findById(id).map(c -> {
            c.setName(input.getName());
            c.setImages(input.getImages());
            return categoryRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @MutationMapping
    @Transactional
    public Boolean deleteCategory(@Argument Long id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // Product update/delete
    @MutationMapping
    @Transactional
    public Product updateProduct(@Argument Long id, @Argument CreateProductInput input) {
        return productRepo.findById(id).map(p -> {
            p.setTitle(input.getTitle());
            p.setQuantity(input.getQuantity());
            p.setDesc(input.getDesc());
            p.setPrice(input.getPrice());
            userRepo.findById(input.getUserId()).ifPresent(p::setUser);

            p.getCategories().clear();
            for (Long catId : input.getCategoryIds()) {
                categoryRepo.findById(catId).ifPresent(p.getCategories()::add);
            }
            return productRepo.save(p);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @MutationMapping
    @Transactional
    public Boolean deleteProduct(@Argument Long id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            return true;
        }
        return false;
    }

}
