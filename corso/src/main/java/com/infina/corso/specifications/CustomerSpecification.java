package com.infina.corso.specifications;

import com.infina.corso.dto.request.CustomerFilterRequest;
import com.infina.corso.model.Customer;
import com.infina.corso.model.enums.CustomerType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CustomerSpecification {

    private CustomerSpecification() {
    }

    public static Specification<Customer> likeName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Customer> likeSurname(String surname) {
        return (root, query, cb) -> {
            if (surname == null || surname.isEmpty()) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
        };
    }

    public static Specification<Customer> likeNameOrSurname(String nameOrSurname) {
        return (root, query, cb) -> {
            if (nameOrSurname == null || nameOrSurname.isEmpty()) {
                return cb.conjunction();
            }

            return cb.or(
                    cb.like(cb.lower(root.get("name")), "%" + nameOrSurname.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("surname")), "%" + nameOrSurname.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Customer> likeCompanyName(String companyName) {
        return (root, query, cb) -> {
            if (companyName == null || companyName.isEmpty()) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("companyName")), "%" + companyName.toLowerCase() + "%");
        };
    }

    public static Specification<Customer> hasTcKimlikNo(String tcKimlikNo) {
        return (root, query, cb) -> {
            if (tcKimlikNo == null || tcKimlikNo.isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("tcKimlikNo"), tcKimlikNo);
        };
    }

    public static Specification<Customer> hasVkn(String vkn) {
        return (root, query, cb) -> {
            if (vkn == null || vkn.isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("vkn"), vkn);
        };
    }

    public static Specification<Customer> hasCustomerType(CustomerType customerType) {
        return (root, query, cb) -> {
            if (customerType == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("customerType"), customerType);
        };
    }

    public static Specification<Customer> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("phone"), phone);
        };
    }

    public static Specification<Customer> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("email"), email);
        };
    }

    // the customer of that broker
    public static Specification<Customer> hasUser(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    // bu alan tek sanırım.
    public static Specification<Customer> hasAccountId(Long accountId) {
        return (root, query, cb) -> {
            if (accountId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("accounts").get("id"), accountId);
        };
    }

    public static Specification<Customer> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Customer> hasCurrencyCode(String currencyCode) {
        return (root, query, cb) -> {
            if (currencyCode == null || currencyCode.isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("currencyCode"), currencyCode);
        };
    }

    public static Specification<Customer> hasCreatedAt(LocalDateTime createdAt) {
        return (root, query, cb) -> {
            if (createdAt == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("createdAt"), createdAt);
        };
    }

    public static Specification<Customer> hasCreatedBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null || end == null) {
                return cb.conjunction();
            }

            return cb.between(root.get("createdAt"), start, end);
        };
    }

    public static Specification<Customer> filterByAllGivenFieldsWithAnd(CustomerFilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                hasUser(filterRequest.getUserId()).toPredicate(root, query, criteriaBuilder),
                likeNameOrSurname(filterRequest.getName()).toPredicate(root, query, criteriaBuilder),
                likeCompanyName(filterRequest.getName()).toPredicate(root, query, criteriaBuilder),
                hasVkn(filterRequest.getVkn()).toPredicate(root, query, criteriaBuilder),
                hasTcKimlikNo(filterRequest.getTcKimlikNo()).toPredicate(root, query, criteriaBuilder),
                hasCustomerType(filterRequest.getCustomerType()).toPredicate(root, query, criteriaBuilder),
                hasAccountId(filterRequest.getAccountId()).toPredicate(root, query, criteriaBuilder),
                hasPhone(filterRequest.getPhone()).toPredicate(root, query, criteriaBuilder),
                hasEmail(filterRequest.getEmail()).toPredicate(root, query, criteriaBuilder),
                hasCreatedBetween(filterRequest.getDateStart(), filterRequest.getDateEnd()).toPredicate(root, query, criteriaBuilder),
                hasStatus(filterRequest.getStatus().name()).toPredicate(root, query, criteriaBuilder),
                hasCurrencyCode(filterRequest.getCurrencyCode()).toPredicate(root, query, criteriaBuilder));
    }

}
