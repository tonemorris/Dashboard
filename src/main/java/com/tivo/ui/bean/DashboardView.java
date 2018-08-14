package com.tivo.ui.bean;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.server.UID;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.tivo.ui.config.BaseUrlProperties;
import com.tivo.ui.config.CustomAuthenticationProvider;
import com.tivo.ui.domain.User;
import com.tivo.ui.domain.UserRoles;
import com.tivo.ui.repository.UserRepository;
import com.tivo.ui.security.IAuthenticationFacade;

@Component
@ManagedBean
@ViewScoped
@Import(BaseUrlProperties.class)
public class DashboardView implements Serializable {

	private static final long serialVersionUID = 1L;
	private DashboardModel model;

	@Value("${baseUrl}") // read from application.yml
	private String baseUrl;

	@Value("${localBaseUrl}") // read from application.yml
	private String localBaseUrl;

	private Map<String, Object> urlMap = new HashMap<String, Object>();
	@Autowired
	private BaseUrlProperties baseUrlProperties;

	private ResponseEntity<String> response;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private User user;
	
	@Autowired
	private CustomAuthenticationProvider authProvider;

	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private String username;
	private String password;
	
	@PostConstruct
	public void init() {
		if(null != authProvider) {
			username = authProvider.getUsername();
			password = authProvider.getPassword();
		}

		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("application.yml"));
		Properties properties = yaml.getObject();

		Object urlMap = properties.get("urlMap");
		model = new DefaultDashboardModel();
		DashboardColumn column1 = new DefaultDashboardColumn();
		DashboardColumn column2 = new DefaultDashboardColumn();
		DashboardColumn column3 = new DefaultDashboardColumn();

		column1.addWidget("sports");
		column2.addWidget("finance");
		column1.addWidget("scoreboard");

		model.addColumn(column1);
		model.addColumn(column2);
		model.addColumn(column3);
	}

	public String createUser() {
		Random rand = new Random();
		user.setPassword(bCryptPasswordEncoder.encode(this.password));
		user.setUsername(username);
		Integer userId = rand.nextInt(1000) + 1;
		user.setUserid(userId.toString());
		
		UserRoles roles = new UserRoles();
		roles.setUserid(userId.toString());
		roles.setRole("admin");
		int userRoleId = rand.nextInt(1000) + 1;
		roles.setUserRoleId(new Integer(userRoleId));
		roles.setUser(user);
		user.setUserRoles(roles);		
		userRepository.save(user);
		return "login";
	}
	
	public String invokeUrl(String xhtmlPage) {

		return baseUrl;
	}

	public void handleReorder(DashboardReorderEvent event) {
		FacesMessage message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		message.setSummary("Reordered: " + event.getWidgetId());
		message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex()
				+ ", Sender index: " + event.getSenderColumnIndex());

		addMessage(message);
	}

	public void handleClose(CloseEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed",
				"Closed panel id:'" + event.getComponent().getId() + "'");

		addMessage(message);
	}

	public void handleToggle(ToggleEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled",
				"Status:" + event.getVisibility().name());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public DashboardModel getModel() {
		return model;
	}

	public String getBaseUrl() {

		return this.baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	private HttpHeaders getHeader() {
		String plainCreds = "amorris:1234";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		return headers;
	}
	
	public String redirectx() throws Exception {
		
		HttpHeaders headers = getHeader();
                
        HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> response = getRestTemplate().exchange("http://localhost:8089/", HttpMethod.GET, request, String.class);
		return response.getBody();
		
		
	}
	
	public String redirect() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        String name = "amorris";
        String password = "1234";
        String authString = name + ":" + password;
        System.out.println("auth string: " + authString);

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        



        HttpServletResponse response = ((HttpServletResponse) facesContext.getExternalContext().getResponse());
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("blicaivens"+req.getHeader("Authorization"));


        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

        session.setAttribute("Authorization", "Basic " + authStringEnc);

        response.setHeader("Authorization", "Basic " + authStringEnc);
        req.setAttribute("Authorization", "Basic " + authStringEnc);

        externalContext.addResponseHeader("Authorization", "Basic " + authStringEnc);
        externalContext.redirect("http://localhost:8089/accountDetails.xhtml?");


        return null;
    }
	
	public RestTemplate getRestTemplate() throws Exception {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
				.build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;

	}

	public Map<String, Object> getUrlMap() {
		return this.urlMap;
	}

	public void setUrlMap(Map<String, Object> urlMap) {
		this.urlMap = urlMap;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}