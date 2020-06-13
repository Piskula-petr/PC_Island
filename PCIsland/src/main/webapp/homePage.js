
// Postupné zobrazování obrázku //////////////////////////////////////////////////////////////////////
	var slideIndex = 0;
	var slides;
	var dots;
	showSlides();

	function showSlides() {
	 	slides = document.getElementsByClassName("slide");
	  	dots = document.getElementsByClassName("pointer");
	  	
	  	var i;
	  	for (i = 0; i < slides.length; i++) {
	    	slides[i].style.display = "none";  
	  	}
	  	slideIndex++;
	  
	  	if (slideIndex > slides.length) {
		  slideIndex = 1
			}    
	  	for (i = 0; i < dots.length; i++) {
	    	dots[i].className = dots[i].className.replace(" active", "");
	  	}
	  	
	  	slides[slideIndex-1].style.display = "block";  
	  	dots[slideIndex-1].className += " active";
	  	setTimeout(showSlides, 5000);
	}
	
// Zobrazení aktuálního obrázku //////////////////////////////////////////////////////////////////////
	function currentSlide(index) {
		if (index > slides.length) {
			index = 1;
		}
		else if (index < 1){
			index = slides.length;
		}
		
		for (i = 0; i < slides.length; i++) {
   			slides[i].style.display = "none";  
		}
		
		for (i = 0; i < dots.length; i++) {
    		dots[i].className = dots[i].className.replace(" active", "");
		}
		
		slides[index-1].style.display = "block";  
		dots[index-1].className += " active";
	}
	