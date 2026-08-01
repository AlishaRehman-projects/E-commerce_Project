package com.example.ecom_proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @NotBlank
        private String name;
        private String description;
        @NotBlank
        private String brand;
        @Positive
        private BigDecimal price;
        private String category;
        private Date releaseDate;
        private boolean available;
        @Min(0)
        private int quantity;
        private String imageName;
        private String imageType;
        @Lob
        private byte[] imageData;

        public Product() {
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getBrand() {
                return brand;
        }

        public void setBrand(String brand) {
                this.brand = brand;
        }

        public BigDecimal getPrice() {
                return price;
        }

        public void setPrice(BigDecimal price) {
                this.price = price;
        }

        public String getCategory() {
                return category;
        }

        public void setCategory(String category) {
                this.category = category;
        }

        public Date getReleaseDate() {
                return releaseDate;
        }

        public void setReleaseDate(Date releaseDate) {
                this.releaseDate = releaseDate;
        }

        public boolean isAvailable() {
                return available;
        }

        public void setAvailable(boolean available) {
                this.available = available;
        }

        public int getQuantity() {
                return quantity;
        }

        public void setQuantity(int quantity) {
                this.quantity = quantity;
        }

        public String getImageName() {
                return imageName;
        }

        public void setImageName(String imageName) {
                this.imageName = imageName;
        }

        public String getImageType() {
                return imageType;
        }

        public void setImageType(String imageType) {
                this.imageType = imageType;
        }

        public byte[] getImageData() {
                return imageData;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public void setImageData(byte[] imageData) {
                this.imageData = imageData;
        }

}
