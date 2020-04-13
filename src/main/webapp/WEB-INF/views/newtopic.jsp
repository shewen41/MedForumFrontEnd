<div class="">
   	<div class="card border-left-primary shadow">
       	<div class="card-body">
       	<form action="" id="topic_form">
       		<div class="md-form md-outline bg grey">
				<input type="text" id="title" name="topic_title" class="form-control">
				<label for="topic_title">Topic Title</label>
			</div>
			<div class="row">
				<div class="col-6">
					<select class="custom-select custom-select-md" id="category_id">
						<option selected>Select Category</option>
						<c:forEach begin="0" end="${categoriesLength -1}" var="i">
							<option value="${categories.getJSONObject(i).get("category_id")}">${categories.getJSONObject(i).getString("category_name")}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-6">
					<select class="custom-select custom-select-md" id="subcategory_id">
						<option selected>Select SubCategory</option>
						<c:forEach begin="0" end="${sub_categoriesLength -1}" var="i">
							<option value="${sub_categories.getJSONObject(i).get("subcategory_id")}">${sub_categories.getJSONObject(i).getString("subcategory_name")}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="md-form mb-4 md-outline">
			  <textarea id="content" class="md-textarea form-control" rows="5"></textarea>
			  <label for="content">Enter details</label>
			</div>
			<div class="d-flex">
				<a href="${contextRoot}/home" class="btn btn-sm btn-danger">Cancel</a>
				<div class="ml-auto">
					<button type="button" id="post_topic" class="btn btn-sm btn-primary">Post</button>
				</div>
				<div id="responseStatus"></div>
			</div>
			
			</form>
       	</div>
          	
	</div>
</div>				


	<!-- SCRIPTS -->
	<script type="text/javascript" src="${js}/tableHTMLExport.js"></script>

	<script type="text/javascript" src="${js}/popper.min.js"></script>

	<script type="text/javascript" src="${js}/mdb.min.js"></script>
	
	<script type="text/javascript" src="${js}/custom.js"></script>
	
	<script>
	
		$("#post_topic").click(function() {
			$("#post_topic").attr("disabled", true);
			let url = "${contextRoot}/addPost";

			let title = $("#title").val();
			let category_id = $("#category_id").val();
			let subcategory_id = $("#subcategory_id").val();
			let content = $("#content").val();
			
			let credentials = {
					"title": title,
					"category_id": category_id,
					"subcategory_id": subcategory_id,
					"content": content
			};
			
			$.ajax({
				method: "POST",
				url: url,
				data: credentials,
				success: function(response){
					if(response == 00){
						//$("#topic_form").reset();
						$("#responseStatus").html('Post added Successfully<i class="far fa-thumbs-up ml-1"></i>').addClass("animated wobble slower text-success");
						setTimeout(function() {
							window.location.href = "${contextRoot}/home";
						}, 5000);
					}
				}	
			})	
		})

	</script>
	

</body>
</html>