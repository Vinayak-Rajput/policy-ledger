package com.hdfclife.ledger.config;

import com.hdfclife.ledger.domain.Customer;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.domain.Rider;
import com.hdfclife.ledger.repo.CustomerRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import com.hdfclife.ledger.repo.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final RiderRepository riderRepository;
    private final Environment environment;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (policyRepository.count() == 0) {

            seedData();
        }

        printStartupSummary();
    }

    private void seedData() {

        Customer c1 = customerRepository.save(new Customer("Anita Sharma", "anita.sharma@hdfclife.example"));
        Customer c2 = customerRepository.save(new Customer("Rahul Mehta", "rahul.mehta@hdfclife.example"));
        Customer c3 = customerRepository.save(new Customer("Priya Nair", "priya.nair@hdfclife.example"));
        Customer c4 = customerRepository.save(new Customer("Vikram Singh", "vikram.singh@hdfclife.example"));
        Customer c5 = customerRepository.save(new Customer("Sneha Patel", "sneha.patel@hdfclife.example"));

        Policy p1001 = new Policy("HDFC-LIFE-1001", c1, "TERM", 18500, "Active");
        Policy p1002 = new Policy("HDFC-LIFE-1002", c2, "ULIP", 42000, "Active");
        Policy p1003 = new Policy("HDFC-LIFE-1003", c3, "ENDOWMENT", 27000, "Lapsed");
        Policy p1004 = new Policy("HDFC-LIFE-1004", c4, "TERM", 15200, "Active");
        Policy p1005 = new Policy("HDFC-LIFE-1005", c5, "ULIP", 36000, "Active");
        Policy p1006 = new Policy("HDFC-LIFE-1006", c1, "ENDOWMENT", 22000, "Pending");

        policyRepository.saveAll(List.of(p1001, p1002, p1003, p1004, p1005, p1006));

        Rider accidentCover = riderRepository.findByCode("ACCIDENT_COVER").orElseThrow();
        Rider waiverPremium = riderRepository.findByCode("WAIVER_OF_PREMIUM").orElseThrow();

        p1001.getRiders().add(accidentCover);
        p1001.getRiders().add(waiverPremium);

        policyRepository.save(p1001);
    }

    private void printStartupSummary() {

        String activeProfile = environment.getActiveProfiles().length > 0 ? environment.getActiveProfiles()[0] : "dev";

        long policyCount = policyRepository.count();
        long customerCount = customerRepository.count();

        String vikramCustomer = policyRepository.findByPolicyNo("HDFC-LIFE-1004")
                .map(p -> p.getCustomer().getFullName())
                .orElse("");

        int activeCount = policyRepository.findByStatusOrderByPolicyNoAsc("Active").size();
        int termCount = policyRepository.findByProductTypeOrderByPolicyNoAsc("TERM").size();
        int anitaCount = policyRepository.findByCustomer_FullNameOrderByPolicyNoAsc("Anita Sharma").size();

        String minPremiumNumbers = policyRepository.findWithPremiumAtLeast(20000).stream()
                .map(Policy::getPolicyNo)
                .collect(Collectors.joining(", "));

        String p1001Riders = policyRepository.findByPolicyNo("HDFC-LIFE-1001")
                .map(p -> p.getRiders().stream().map(Rider::getCode).sorted().collect(Collectors.joining(", ")))
                .orElse("");

        System.out.println("Active profile -> " + activeProfile);
        System.out.println("Policy count -> " + policyCount);
        System.out.println("Customer count -> " + customerCount);
        System.out.println("Lookup HDFC-LIFE-1004 customer -> " + vikramCustomer);
        System.out.println("Active count -> " + activeCount);
        System.out.println("TERM count -> " + termCount);
        System.out.println("Anita Sharma policy count -> " + anitaCount);
        System.out.println("minPremium=20000 policy numbers -> " + minPremiumNumbers);
        System.out.println("HDFC-LIFE-1001 rider codes -> " + p1001Riders);
    }
}