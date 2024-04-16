package com.profile.in.service.imp;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.profile.in.model.OtpConfig;
import com.profile.in.repository.EmailConfigRepository;
import com.profile.in.repository.OtpConfigRepository;
import com.profile.in.repository.UserDetailsRepo;
import com.profile.in.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Service("userService")
public class UserServiceImp implements UserService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;
    @Autowired
    private EmailConfigRepository emailConfigRepository;

    @Autowired
    private OtpConfigRepository otpConfigRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserServiceImpl.loadUserByUsername()");

        Optional<com.profile.in.model.UserDetails> optional = userDetailsRepo.findByUname(username);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        } else {

            com.profile.in.model.UserDetails details = optional.get();

            User user = new User(details.getUname(), details.getPwd(), details.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet()));
            return user;
        }
    }

    @Override
    public String regsiter(com.profile.in.model.UserDetails details) {
        System.out.println("UserServiceImpl.regsiter()");

        details.setPwd(encoder.encode(details.getPwd()));

        return userDetailsRepo.save(details).getUid() + " UserId is registered";
    }

    @Override
    public Map<String, Object> username(String username) {
        AtomicReference<Map<String, Object>> map = new AtomicReference<>(new HashMap<>());
        userDetailsRepo.findByUname(username).ifPresentOrElse(
                (userDetails) -> {
                    map.set(sendOtp(userDetails));
                    map.get().put("status", true);
                },
                () -> {
                    map.get().put("status", false);
                }
        );
        return map.get();
    }


    public Map<String, Object> sendOtp(com.profile.in.model.UserDetails userDetails) {
        Map<String, Object> map = new HashMap<>();
        emailConfigRepository.findAllByMailConfigId("gkpsomanathsingh@gmail.com").ifPresentOrElse(
                (mailConfig) -> {
                    // Your action when email configurations are found
                    // For example:
                    String recipientEmail = userDetails.getEmail();
                    map.put("email", recipientEmail);
                    // Generate OTP
                    Random random = new Random();
                    int otpValue = 1000 + random.nextInt(9000);
                    String otp = String.valueOf(otpValue);

                    // Sender's email address and password
                    final String senderEmail = mailConfig.getFromMail();
                    final String password = mailConfig.getPassword();

                    // Sending email
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", mailConfig.getMailSmtpAuth());
                    props.put("mail.smtp.starttls.enable", mailConfig.getMailSmtpStarttlsEnable());
                    props.put("mail.smtp.host", mailConfig.getMailSmtpHost()); // Your SMTP server
                    props.put("mail.smtp.port", mailConfig.getMailSmtpPort()); // SMTP port (typically 587 for TLS/STARTTLS)

                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(senderEmail, password);
                        }
                    });

                    try {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(senderEmail));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                        message.setSubject("Your OTP");
                        message.setText("Your One-Time Password (OTP) is: " + otp);

                        Transport.send(message);
                        map.put("massage", "OTP sent successfully.");
                        map.put("otp_status", true);
                        System.out.println("OTP sent successfully.");
                        otpConfig(userDetails.getUname(), otp);
                    } catch (MessagingException e) {
                        System.out.println(e.getMessage());
                        map.put("otp_status", false);
                        map.put("errorMsg", e.getMessage());
                    }
                    System.out.println("Email configurations found: " + mailConfig);
                },
                () -> {
                    // Your action when no email configurations are found
                    // For example:
                    map.put("massage", "No email configurations found for the specified ID");
                    System.out.println("No email configurations found for the specified ID");
                }
        );
        return map;
    }

    private void otpConfig(String uname, String otp) {
        otpConfigRepository.findAllByUsername(uname).ifPresentOrElse(
                (otpConfig) -> {
                    otpConfig.setUsername(uname);
                    otpConfig.setOtp(otp);
                    otpConfig.setStatus(true);
                    otpConfigRepository.save(otpConfig);
                },
                () -> {
                    OtpConfig otpConfig = new OtpConfig();
                    otpConfig.setUsername(uname);
                    otpConfig.setOtp(otp);
                    otpConfig.setStatus(true);
                    otpConfigRepository.save(otpConfig);
                }
        );
    }


    @Override
    public Map<String, Object> forgetPassword(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        userDetailsRepo.findByUname(username).ifPresentOrElse(
                (userDetails) -> {
                    userDetails.setPwd(encoder.encode(password));
                    userDetailsRepo.save(userDetails);
                    map.put("Status", true);
                },
                () -> {
                    map.put("Status", false);
                }
        );
        return map;
    }

    @Override
    public Map<String, Object> otpVerify(String username, String otp) {
        Map<String, Object> map = new HashMap<>();
        otpConfigRepository.findAllByUsername(username).ifPresentOrElse(
                (otpConfig) -> {
                    if (otpConfig.getOtp().matches(otp) && otpConfig.getStatus()) {
                        otpConfig.setStatus(false);
                        otpConfigRepository.save(otpConfig);
                        map.put("status", true);
                    } else {
                        map.put("status", false);
                        map.put("msg", "Invalid Otp");
                    }
                },
                () -> {
                    map.put("Status", false);
                }
        );
        return map;
    }
}
