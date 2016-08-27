/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.spring.mvc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.antuansoft.async.FutureHandler;
import com.antuansoft.async.NonBlockingExecutor;
import com.antuansoft.async.NonBlockingFuture;
import com.antuansoft.herospace.*;
import com.antuansoft.init.WebAppConfig;
import com.antuansoft.mongodb.connection.UserRepository;
import com.antuansoft.mongodb.domain.Order;
import com.antuansoft.mongodb.domain.User;
import com.antuansoft.service.ClientService;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.antuansoft.mongodb.domain.Campaign;
import com.antuansoft.mongodb.domain.Contact;
import com.antuansoft.mongodb.repositories.CampaignRepositoryDao;
import com.antuansoft.mongodb.repositories.ContactRepo;
import com.antuansoft.mongodb.repositories.UserRepositoryDao;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author anjana.m
 */
@Controller
public class MainController {

    @Autowired
    private UserRepositoryDao userRepositoryDao;

    @Autowired
    private CampaignRepositoryDao campaignRepositoryDao;

    @Autowired
    private ContactRepo contactRepo;
    //    @Autowired
    UserRepository userRepository;

    //@Autowired
    ClientService clientService;

    @Autowired
    QuoteService quoteService;


    private static final Logger logger = Logger.getLogger(MainController.class);

    public MainController() throws UnknownHostException {

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaultPage(ModelMap map) {

        return "redirect:/menu";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(ModelMap model) {
        return "logout";
    }

    @RequestMapping(value = "/accessdenied")
    public String loginerror(ModelMap model) {
        model.addAttribute("error", "true");
        return "denied";
    }


    @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
    public String listUsers(ModelMap map) {
        Iterable<Contact> contacts = contactRepo.findAll();
        System.out.println(contacts.iterator().next().getfName());

        return "hero";
    }


    @RequestMapping(value = "/listCampaigns", method = RequestMethod.GET)
    public String listCampaigns(ModelMap map) {

        map.addAttribute("new_campaign", new Campaign());
        Iterable<Campaign> campaings = campaignRepositoryDao.findAll();
        map.addAttribute("campaigns", campaings);

        return "listCampaigns";
    }

    @PreAuthorize("hasRole('ROLE_CAMPAIGN')")
    @RequestMapping(value = "/addCampaing", method = RequestMethod.POST)
    public String addCampaign(
            @ModelAttribute(value = "new_campaign") Campaign new_campaign,
            BindingResult result) {

        new_campaign.setId(UUID.randomUUID().toString());
        campaignRepositoryDao.save(new_campaign);

        return "redirect:/listCampaigns";
    }

    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Order> sowMenuss(HttpServletRequest request) throws IOException {
        //String name = (String) request.getSession().getAttribute("databaseConfig");
        userRepository = new UserRepository(request);
        List<Order> orders = new ArrayList<Order>();
        orders = userRepository.findAll();
        return orders;
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Person> testme(HttpServletRequest request) throws IOException, InterruptedException, ExecutionException {
        //this is the main success page and we set all the services
        setAllServices();

        clientService.sayMeHero();

        Person p = new Person();
        p.lastName = "John";
        p.firstName = "Carter";
        Address address = new Address();
        address.street = "Palace";
        address.city = "City";
        address.postal = "12345";
        address.state = "Kingdom";
        address.country = "Wonderland";
        p.address = address;
        p.tags.add("flying carpet");
        p.tags.add("magic lamp");
        p.tags.add("fantasy");
        p.tags.add("ecstacy");


      /*  String hero=dao.getCollectionSuffix();
        dao.save(p);
        ObjectId id = p.id;
        Person find = dao.find(id.toString());
        dao.update(id,p);*/

        //  dao.delete("57322bb6ed261d33e1cf49d2");


        List<GrantedAuthority> xx = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();


        for (GrantedAuthority g : xx) {
            System.out.println(g.toString());
        }
        User user = AuthenticationUtil.getCurrentUser();
        clientService.sayMeHero();
        Future<Person> personFuture = clientService.findUser("e");

        String personName = personFuture.get().firstName;
        System.out.println(personName);
        personFuture.isDone();
        return clientService.getAllPersons();
    }

    // @Async
    @RequestMapping("/helloAsync")
    @ResponseBody
    public Callable<List<Person>> sayHelloAsync(final HttpServletRequest request) {
        User user1 = AuthenticationUtil.getCurrentUser();

        final SecurityContext zz = SecurityContextHolder.getContext();
        zz.setAuthentication(SecurityContextHolder.getContext().getAuthentication());
        logger.info("Entering controller");

        String s = "hero";
        Callable<List<Person>> asyncTask = new Callable<List<Person>>() {

            @Override
            public List<Person> call() throws Exception {
                request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, zz);
                User user = AuthenticationUtil.getCurrentUser();
                return clientService.getAllPersons();
            }
        };

        logger.info("Leaving  controller");
        User anotherUser = AuthenticationUtil.getCurrentUser();
        return asyncTask;
    }


    /*@RequestMapping("/futureAsync")
    @ResponseBody
    public Callable<List<Person>> sayHelloAsync() {
        logger.info("Entering controller");

        Callable<List<Person>> asyncTask = new Callable<List<Person>>() {

            @Override
            public List<Person> call() throws Exception {
                return clientService.getAllPersons();
            }
        };

        logger.info("Leaving  controller");
        return asyncTask;
    }*/


    @Secured("ROLE_ADMIN")
    @RequestMapping("/test")

    public String tets() {
        //  quoteService.sendNotification();
        return "heroine";
    }

    @RequestMapping("/socket")
    public String rocket() {
        return "socket";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/anotherTest")
    @ResponseBody
    public String anotherTest() throws Exception {
        Callable<String> v = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hey you";
            }
        };

        return v.call();
    }


    private void setAllServices() throws UnknownHostException {
        ClientService clientService = new ClientService();
        this.clientService = clientService;

    }

    @RequestMapping("/future")
    @ResponseBody
    public String anotherTestzaaa() throws Exception {
        NonBlockingExecutor executor =
                new NonBlockingExecutor(Executors.newSingleThreadExecutor());


        executor.submitNonBlocking(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(2000);

                int rnd = new Random().nextInt(10);
                if (rnd < 5) return rnd;
                else throw new IllegalStateException("MUST BE LESS THAN 5 BUT " + rnd);
            }
        }).setHandler(new FutureHandler<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                System.out.print("Done Kale Done");
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });

//        future.setHandler(new FutureHandler<Integer>() {
//            @Override
//            public void onSuccess(Integer value) {
//                System.out.println("LESS THAN 5 = " + value);
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                System.out.println(e.getMessage());
//            }
//        });

        Thread.sleep(1000);
        System.out.println("Done !!!!");
        return "done";
    }

    @RequestMapping("/hero")
    @ResponseBody
    public void herooo() throws Exception {
        NonBlockingExecutor executor =
                new NonBlockingExecutor(Executors.newSingleThreadExecutor());

        executor.submitNonBlocking(new Callable<Person>() {
            @Override
            public Person call() throws Exception {
                Person p = new Person();
                p.lastName = "Leonardo";
                p.firstName = "Da Vinvi";
                Address address = new Address();
                address.street = "Palace";
                address.city = "City";
                address.postal = "12345";
                address.state = "Kingdom";
                address.country = "Wonderland";
                p.address = address;
                p.tags.add("flying carpet");
                p.tags.add("magic lamp");
                p.tags.add("fantasy");
                p.tags.add("ecstacy");

                return clientService.savePerson(p);

            }
        }).setHandler(new FutureHandler<Person>() {
            @Override
            public void onSuccess(Person result) {
                System.out.print("See this" + result.firstName);


                //quoteService.sendNotification();

            }

            @Override
            public void onFailure(Throwable e) {
                System.out.print("Failed to do");

            }
        });

//        future.setHandler(new FutureHandler<Integer>() {
//            @Override
//            public void onSuccess(Integer value) {
//                System.out.println("LESS THAN 5 = " + value);
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                System.out.println(e.getMessage());
//            }
//        });

    }
}

