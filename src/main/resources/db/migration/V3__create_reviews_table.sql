CREATE TABLE IF NOT EXISTS REVIEWS (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  order_id BIGINT NOT NULL,
  rating DECIMAL(3,1) NOT NULL,
  comment TEXT,
  created_at DATETIME(6) NOT NULL,
  deleted_at DATETIME(6),
  updated_at DATETIME(6),

  CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products(id),
  CONSTRAINT fk_reviews_order FOREIGN KEY (order_id) REFERENCES orders(id)
);