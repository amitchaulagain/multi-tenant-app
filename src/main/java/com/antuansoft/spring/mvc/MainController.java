/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.spring.mvc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    UserRepository userRepository ;
    PersonDAO dao;
    @Autowired
    ClientService clientService;


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
    List<Person> testme(HttpServletRequest request) throws IOException {

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
        List<Person> persons=dao.list();
        String ss= (String) MyThreadLocalStuff.get();

      //  dao.delete("57322bb6ed261d33e1cf49d2");


       List<GrantedAuthority>xx= (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();


        for(GrantedAuthority g:xx){
            System.out.println(g.toString());
        }
        User user=AuthenticationUtil.getCurrentUser();



        return  persons;
    }


}
