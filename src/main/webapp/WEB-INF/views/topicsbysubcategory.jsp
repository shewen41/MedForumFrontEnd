						    <c:choose>
						    <c:when test="${topicbysubcatLength > 0}">
						    <c:forEach begin="0" end="${topicbysubcatLength -1}" var="i">
								<div class="card border-left-primary shadow py-2 mb-2">
						        	<div class="card-body">
						            	<div class="row no-gutters align-items-center">
						                	<div class="col mr-2">
						                 		<div class="text-xs font-weight-bold text-primary text-uppercase mb-1"><h4><a href="${contextRoot}/post/${topicbysubcat.getJSONObject(i).getInt('topic_id')}" class="text-primary">${topicbysubcat.getJSONObject(i).getString("title")}</a></h4></div>
						                 		<div class="h5 mb-0 font-wx , eight-bold text-gray-800">By ${topicbysubcat.getJSONObject(i).getString("author")}</div>
						               		</div>
						               		<div class="col-auto">
						                 		<i class="fas fa-calendar fa-2x text-gray-300"></i>
						               		</div>
						             	</div>
						             </div>
						             <div class="card-footer">
						    		 	<div class="row no-gutters align-items-center">
							             	<div class="col mr-2">
							             		<a href="#">${topicbysubcat.getJSONObject(i).getInt("topic_comments")}<i class="far fa-comments mr-4"></i></a> 
							             		<a href="#">21<i class="far fa-thumbs-up"></i></a> 
							             	</div>
						             		<div class="col-auto">
						             			<span class="">${topicbysubcat.getJSONObject(i).getString("date")}</span>
						             		</div>
						             	</div>
						             </div>
								</div>
							</c:forEach>
							</c:when>
							<c:otherwise>
								<h2>No Post For this SubCategory</h2>
							</c:otherwise>
							</c:choose>