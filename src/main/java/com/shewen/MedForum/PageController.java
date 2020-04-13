package com.shewen.MedForum;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.shewen.MedForum.service.RestCall;


@Controller
public class PageController {
	
		RestCall restCall = new RestCall();
	

		@RequestMapping(value= {"/", "index"})
		public ModelAndView index() {		
			ModelAndView mv = new ModelAndView("index");

			return mv;	
		}
		
		@RequestMapping(value= {"/home"})
		public ModelAndView home(HttpServletRequest request) throws JsonParseException, JSONException {		
			ModelAndView mv = new ModelAndView("home");
			
			String url = "http://localhost:8080/MedForumRestServices/json/data/topics/titles";
			String categories_url = "http://localhost:8080/MedForumRestServices/json/data/categories";
			String subcategories_url = "http://localhost:8080/MedForumRestServices/json/data/subcategories";
			
			String user = (String)request.getSession().getAttribute("username");
			
			System.out.println("user home "+ user);
			
			try {
				JSONArray topictitles = new JSONArray(restCall.executeGet(url));
				JSONArray categories = new JSONArray(restCall.executeGet(categories_url));
				int categoriesLength = categories.length();
				JSONArray sub_categories = new JSONArray(restCall.executeGet(subcategories_url));
				int sub_categoriesLength = sub_categories.length();
				
				System.out.println("category "+ categories);
				System.out.println("sub category "+ sub_categories );
				
				int topictitlesLength = topictitles.length();
				
				mv.addObject("topictitles", topictitles);
				mv.addObject("topictitlesLength", topictitlesLength);
				mv.addObject("categories", categories);
				mv.addObject("categoriesLength", categoriesLength);
				mv.addObject("sub_categories", sub_categories);
				mv.addObject("sub_categoriesLength", sub_categoriesLength);
				mv.addObject("user", user);
				mv.addObject("userClickHome", true);
				
				return mv;
			}
			catch(Exception e) {
				e.printStackTrace();
				return mv;
			}
		}
		
		@RequestMapping(value= {"/newtopic"})
		public ModelAndView newTopic(HttpServletRequest request, HttpServletResponse response) {
			
			String user = (String)request.getSession().getAttribute("username");
			
			String categories_url = "http://localhost:8080/MedForumRestServices/json/data/categories";
			
			String subcategories_url = "http://localhost:8080/MedForumRestServices/json/data/subcategories";
			
			ModelAndView mv = new ModelAndView("home");
			
			try {
				if(user != null) {
					JSONArray categories = new JSONArray(restCall.executeGet(categories_url));
					int categoriesLength = categories.length();
			
					JSONArray sub_categories = new JSONArray(restCall.executeGet(subcategories_url));
					int sub_categoriesLength = sub_categories.length();
					
					mv.addObject("categories", categories);
					mv.addObject("categoriesLength", categoriesLength);
					mv.addObject("sub_categories", sub_categories);
					mv.addObject("sub_categoriesLength", sub_categoriesLength);
					mv.addObject("user", user);
					mv.addObject("userClickNewTopic", true);
					//mv.setViewName("home");
		
					return mv;
				}
				else {
					
					response.sendRedirect("/MedForum/home");
					return null;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return mv;
			}
		}
		
		@RequestMapping(value= {"/post/{id}"})
		public ModelAndView topicDetailsPage(HttpServletRequest request, HttpServletResponse response, @PathVariable(name = "id") int id) throws IOException {
			//System.out.println(id);
			String user = (String)request.getSession().getAttribute("username");
			
			String url = "http://localhost:8080/MedForumRestServices/json/data/topicsById";
			
			String replies_url = "http://localhost:8080/MedForumRestServices/json/data/repliesByTopicId";
			
			String categories_url = "http://localhost:8080/MedForumRestServices/json/data/categories";
			
			try {
				JSONObject js = new JSONObject();
				js.put("topic_id", id);
				
				JSONObject resp = new JSONObject(restCall.processRequest(url, js));
				
				JSONArray rep_resp = new JSONArray(restCall.processRequest(replies_url, js));
				
				JSONArray categories = new JSONArray(restCall.executeGet(categories_url));
				int categoriesLength = categories.length();
				
				System.out.println("topicDetails" + resp);
				System.out.println(rep_resp.length());
				System.out.println("replies "+ rep_resp);
				
				ModelAndView mv = new ModelAndView("home");
				
				mv.addObject("topic_details", resp);
				mv.addObject("rep_resp", rep_resp);
				mv.addObject("rep_respLen", rep_resp.length());
				mv.addObject("categories", categories);
				mv.addObject("categoriesLength", categoriesLength);
				mv.addObject("user", user);
				mv.addObject("userClickTopicDetails", true);
	
				return mv;
			}
			catch(Exception e) {
				e.printStackTrace();
				response.sendRedirect("/MedForum/home");
				return null;
			}
		}
		
		@RequestMapping(value = {"/addPost"}, method=RequestMethod.POST)
		@ResponseBody
		public String addPost(@RequestParam("title") String title, @RequestParam("category_id") int category_id, @RequestParam("subcategory_id") int subcategory_id, @RequestParam("content") String content) {
			
			String author = "Shewen";
			
			System.out.println(title + category_id + subcategory_id + content + author);
			
			String addTopic_url = "http://localhost:8080/MedForumRestServices/json/data/topics";
			
			JSONObject js = new JSONObject();
			
			js.put("category_id", category_id);
			js.put("subcategory_id", subcategory_id);
			js.put("author", author);
			js.put("title", title);
			js.put("content", content);

			try {
				JSONObject addRes = new JSONObject(restCall.processRequest(addTopic_url, js));
				
				String responsecode = addRes.getString("responsecode");
				
				if(Integer.parseInt(responsecode) == 00) {
					return responsecode;
				}
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@RequestMapping(value = {"/addComment"}, method=RequestMethod.POST)
		@ResponseBody
		public String addComment(@RequestParam("user") String author, @RequestParam("topic_id") String topic_id,  @RequestParam("reply_content") String comment) {
			
			//String author = "Shewen";
			
			System.out.println(topic_id + comment + author);
			
			String addComment_url = "http://localhost:8080/MedForumRestServices/json/data/replies";
			
			JSONObject js = new JSONObject();
			
			js.put("author", author);
			js.put("topic_id", topic_id);
			js.put("comment", comment);

			try {
				JSONObject addRes = new JSONObject(restCall.processRequest(addComment_url, js));
				
				String responsecode = addRes.getString("responsecode");
				
				System.out.println("responsecode "+ responsecode);
		
				return responsecode;
				
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@RequestMapping(value= {"/postBySubCat/{id}"})
		public ModelAndView topicsBySubCat(HttpServletRequest request, HttpServletResponse response, @PathVariable(name = "id") int id) throws IOException {
			System.out.println(id);
			String user = (String)request.getSession().getAttribute("username");
			
			String url = "http://localhost:8080/MedForumRestServices/json/data/topics/subcategory/titles";
			
			String replies_url = "http://localhost:8080/MedForumRestServices/json/data/repliesByTopicId";
			
			String categories_url = "http://localhost:8080/MedForumRestServices/json/data/categories";
			
			try {
				JSONObject js = new JSONObject();
				js.put("subcategory_id", id);
				
				JSONArray resp = new JSONArray(restCall.processRequest(url, js));
				
				JSONArray rep_resp = new JSONArray(restCall.processRequest(replies_url, js));
				
				JSONArray categories = new JSONArray(restCall.executeGet(categories_url));
				int categoriesLength = categories.length();
				
				System.out.println("topicBySubCat" + resp);
				//System.out.println(rep_resp.length());
				//System.out.println("replies "+ rep_resp);
				
				ModelAndView mv = new ModelAndView("home");
				
				mv.addObject("topicbysubcat", resp);
				mv.addObject("topicbysubcatLength", resp.length());
				mv.addObject("rep_respLen", rep_resp.length());
				mv.addObject("categories", categories);
				mv.addObject("categoriesLength", categoriesLength);
				mv.addObject("user", user);
				mv.addObject("userClickTopicBySubCat", true);
	
				return mv;
			}
			catch(Exception e) {
				e.printStackTrace();
				response.sendRedirect("/MedForum/home");
				return null;
			}
		}

		
		@RequestMapping("/login")
		@ResponseBody
		public String login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("pass") String password) {
			
			String login1_url = "http://localhost:8080/MedForumRestServices/json/data/login1";
			
			try {
				JSONObject js = new JSONObject();
				js.put("username", username);
				
				JSONObject response = new JSONObject(restCall.processRequest(login1_url, js));
				
				System.out.println("response " + response);
				
				String responsecode = response.getString("responsecode");
				if(responsecode.equals("00")) {
					String token = response.getString("token");
					String login2_url = "http://localhost:8080/MedForumRestServices/json/data/login2";
					JSONObject js_2 = new JSONObject();
					js_2.put("token", token);
					js_2.put("password", password);
					
					JSONObject response_2 = new JSONObject(restCall.processRequest(login2_url, js_2));
					
					System.out.println("response 2 "+ response_2 );
					String responsecode_2 = response_2.getString("responsecode");
					if(responsecode_2.equals("00")) {
						request.getSession().setAttribute("id", response_2.get("user_id"));
						request.getSession().setAttribute("token", response.get("token"));
						request.getSession().setAttribute("username", response_2.get("username"));
						
						return "success";
					}
					else {
						return "password_error";
					}
				}
				else {
					return "username_error";
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return "username_error";
			}
			
		}
	

		@RequestMapping("/register")
		@ResponseBody
		public String register(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("pass") String password) {
			
			String role = "USER";
			String reg_url = "http://localhost:8080/MedForumRestServices/json/data/register";
			
			try {
				JSONObject js = new JSONObject();
				js.put("username", username);
				js.put("role", role);
				js.put("password", password);
				js.put("email", email);
				
				JSONObject response = new JSONObject(restCall.processRequest(reg_url, js));
				
				String responsecode = response.getString("responsecode");
				
				System.out.println("responseReg "+ responsecode);
				
				return responsecode;
				
				
			}
			catch(Exception e) {
				e.printStackTrace();
				return "error";
			}
			
		}
		
		@RequestMapping("/subCategory")
		@ResponseBody
		public String subCategory(HttpServletRequest request) {
			
		 String subcategories_url = "http://localhost:8080/MedForumRestServices/json/data/subcategories";
			
			try {
				
				
				String response = restCall.executeGet(subcategories_url);
				
				System.out.println("responseReg "+ response);
				
				return response;
				
				
			}
			catch(Exception e) {
				e.printStackTrace();
				return "error";
			}
			
		}
		
		@RequestMapping("/logout")
		public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
			request.getSession().invalidate();
			response.sendRedirect("/MedForum/home");
			return null;
		}

		
}
