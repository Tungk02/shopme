package com.shopme.admin.review;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTests {

	@Autowired private ReviewRepository repo;
	
	@Test
	public void testCreateReview() {
		Integer productId = 73;
		Product product = new Product(productId);
		
		Integer CustomerId = 20;
		Customer customer = new Customer(CustomerId);
		
		Review review = new Review();
		review.setProduct(product);
		review.setCustomer(customer);
		review.setReviewTime(new Date());
		review.setHeadline("Good for my needs. I belie!");
		review.setComment("The Dell Inspiron 3000 15.6-inch HD Touchscreen Laptop offers solid performance and convenience for everyday use, but may not be suitable for demanding tasks like gaming or video editing. Overall rating: 4 stars.");
		review.setRating(4);
		
		Review savedReview = repo.save(review);
		
		assertThat(savedReview).isNotNull();
		assertThat(savedReview.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListReview() {
		List<Review> listReviews = repo.findAll();
		
		assertThat(listReviews.size()).isGreaterThan(0);
		
		listReviews.forEach(System.out::println);
	}
	
	@Test
	public void testGetReview() {
		Integer id = 2;
		Review review = repo.findById(id).get();
		
		assertThat(review).isNotNull();
		
		System.out.println(review);
	}
	
	@Test
	public void testUpdateReview() {
		Integer id = 1;
		String headline = "An awesome camera at an awesome price";
		String comment = "Overall great camera and is highly capable";
		
		Review review = repo.findById(id).get();
		review.setHeadline(headline);
		review.setComment(comment);
		
		Review updatedReview = repo.save(review);
		
		assertThat(updatedReview.getHeadline()).isEqualTo(headline);
		assertThat(updatedReview.getComment()).isEqualTo(comment);
	}
	
	@Test
	public void testDeleteview() {
		Integer id = 1;
		repo.deleteById(id);
		
		Optional<Review> findById = repo.findById(id);
		
		assertThat(findById).isNotPresent();
	}
}
