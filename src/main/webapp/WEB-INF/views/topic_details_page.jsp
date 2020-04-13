<div>
	<div class="card border-left-primary shadow py-2 mb-2">
		<div class="card-header">
			<div class="d-flex">
				<h2 class="col-8">${topic_details.getString("title")}</h2>
				<span class="col-4 text-center"><button class="btn btn-sm btn-info">FOLLOW TOPIC</button></span>
			</div>
			<div class="container mt-2">
				<p class="mb-0 py-0">${topic_details.getString("date")}, no of followers</p>
			</div>
		</div>
       	<div class="card-body">
           	<div class="container">
           		<div class="mb-2">
           			<i class="fas fa-user fa-1x mr-1"></i>${topic_details.getString("author")}
           		</div>
           		<div class="d-none" id="topic_id">${topic_details.get("topic_id")}</div>
           		<div>
           			<p class="mt-4">${topic_details.getString("content")}</p>
               	</div>
               	<div>
               		<c:if test="${rep_respLen > 0}">
               			<span>${rep_respLen}<i class="far fa-comments mr-4"></i></span>
               		</c:if> 
             		<span>21<i class="far fa-thumbs-up"></i></span>
               	</div>
            	</div>
		</div>
        <div class="card-footer">
        	<div class="row no-gutters">
        	<c:choose>
        		<c:when test="${user eq null}">
        			<a href="" class="btn btn-sm btn-grey" data-toggle="modal" data-target="#modalLoginForm"><span style="font-size: 15px">NEW TOPIC</span></a>
        			<a href="" class="ml-auto mr-3" data-toggle="modal" data-target="#modalLoginForm"><span class="btn btn-sm btn-primary"><i class="far fa-comments fa-2x mr-2"></i><strong style="font-size: 15px">Reply</strong></span></a>
        			<a href="" class="mt-2" data-toggle="modal" data-target="#modalLoginForm"><i class="far fa-thumbs-up fa-2x"></i><span style="font-size: 15px">LIKE</span></a>
        		</c:when>
        		<c:otherwise>
	        		<a href="${contextRoot}/newtopic" class="btn btn-sm btn-grey"><span style="font-size: 15px">NEW TOPIC</span></a>
	        		<a href="#comment_card" class="ml-auto mr-3" id="reply_btn"><span class="btn btn-sm btn-primary"><i class="far fa-comments fa-2x mr-2"></i><strong style="font-size: 15px">Reply</strong></span></a>
	        		<a href="" class="mt-2"><i class="far fa-thumbs-up fa-2x"></i><span style="font-size: 15px">LIKE</span></a>
        		</c:otherwise>
        	</c:choose>
        	</div>
        </div>
	</div>
	<div class="" id="comment_box">
		<div class="card">
			<div class="card-header comment_card">
				<i class="fas fa-user fa-1x mr-1"></i><span id="logged_user">${user}</span>
			</div>
			<div class="card-body">
				<div class="md-form mt-0 pt-0 mb-4 md-outline">
			  		<textarea id="reply_content" class="md-textarea form-control" rows="5"></textarea>
			  		<label for="reply_content">Type your reply</label>
				</div>
				<div class="row">
					<a class="btn btn-sm btn-info ml-auto mr-3" id="post_reply_btn"><span style="font-size: 15px">POST REPLY</span></a>
				</div>
			</div>
		</div>
	</div>
	
	<div id="responseStatus"></div>
	
	<div class="mt-5 mb-4 border border-dark border-right-0 border-left-0 border-top-0">
		<h2>${rep_respLen} Replies</h2>
	</div>
	<c:if test="${rep_respLen > 0}">
		<c:forEach begin="0" end="${rep_respLen - 1}" var="i">
		<div class="card mb-1">
			<div class="card-body">
	           	<div class="container">
	           		<div class="mb-2">
	           			<i class="fas fa-user fa-1x mr-1"></i>${rep_resp.getJSONObject(i).getString("author")}
	           		</div>
	           		<div>
	           			<p class="mt-4">${rep_resp.getJSONObject(i).getString("comment")}</p>
	               	</div>
	            </div>
			</div>
            <div class="card-footer">
            	<div class="d-flex row">
	             	<a href="" class="btn btn-sm"><i class="fas fa-quote-left mr-1"></i>Quote<i class="fas fa-quote-right ml-1"></i></a>
	             	<div class="ml-auto mt-2 mr-2">${rep_resp.getJSONObject(i).getString("date")}</div>
            	</div>
            </div>
		</div>
		</c:forEach>
	</c:if>				
</div>	

	
	<!-- SCRIPTS -->
	<script type="text/javascript" src="${js}/tableHTMLExport.js"></script>

	<script type="text/javascript" src="${js}/popper.min.js"></script>

	<script type="text/javascript" src="${js}/mdb.min.js"></script>

	<script type="text/javascript" src="${js}/custom.js"></script>
	
	<script>
	
		$("#comment_box").hide();
		
		$("#reply_btn").click(function() {
			$("#comment_box").toggle();
		})
		
		
		$("#post_reply_btn").click(function() {
			$("#post_reply_btn").attr("disabled", true);
			let url = "${contextRoot}/addComment";
			
			let user = $("#logged_user").html();
			let topic_id = $("#topic_id").html();
			let reply_content = $("#reply_content").val();
			//let subcategory_id = $("#subcategory_id").val();
			//let content = $("#content").val();
			
			let credentials = {
					"user" : user,
					"topic_id": topic_id,
					"reply_content": reply_content
					//"subcategory_id": subcategory_id,
					//"content": content
			};
			
			$.ajax({
				method: "POST",
				url: url,
				data: credentials,
				success: function(response){
					if(response == 00){
						//$("#topic_form").reset();
						//location.reload();
						$("#responseStatus").html('Reply added Successfully<i class="far fa-thumbs-up ml-1"></i>').addClass("animated heartBeat slower text-success");
						setTimeout(function() {
							location.reload();
						}, 2000);
					}
				}	
			})	
		})
		
	</script>
	

</body>
</html>