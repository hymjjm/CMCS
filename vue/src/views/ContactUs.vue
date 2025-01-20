<template>
  <div>
  <div style="display: flex; justify-content: center; align-items: center; height: 50vh;">
    <div class="contact-info">
      <h1>Contact Us</h1>
      <p>
        If you have any questions or inquiries, please feel free to contact us using the following information:
      </p>
      <ul class="contact-list">
        <li>
          <strong>Email:</strong> <a class="contact-link" href="mailto:tmliang@njnu.edu.cn">tmliang@njnu.edu.cn</a>
          <strong>Email:</strong> <a class="contact-link" href="mailto:lguo@njupt.edu.cn">lguo@njupt.edu.cn</a>
        </li>
        <li>
          <strong>Address:</strong> State Key Laboratory of Organic Electronics and Information Displays & Institute of Advanced Materials (IAM), Nanjing University of Posts & Telecommunications, 9 Wenyuan Road, Nanjing 210023, China
        </li>
      </ul>
    </div>

  </div>
  <div class="copyright">
    <p>&copy; 2024 <a class="copyright-link" href="http://njupt.edu.cn/">Guo Lab, State Key Laboratory of Organic Electronics and Information Displays & Institute of Advanced Materials (IAM), Nanjing University of Posts & Telecommunications, Nanjing, China</a>.</p>
    <p>&copy; 2024 <a class="copyright-link" href="http://sky.njnu.edu.cn/cn/">Liang Lab, College of Life Science, Nanjing Normal University, Nanjing, China</a></p>
  </div>
  </div>
</template>

<script>
export default {
  name: 'ContactUs',
  data() {
    return {
      msg: '联系页面'
    };
  }
};
</script>

<style scoped>
.contact-info {
  text-align: center;
  font-family: Arial, sans-serif;
  background-color: #f2f2f2;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.2);
}

.contact-info h1 {
  font-size: 28px;
  color: #333;
}

.contact-info p {
  font-size: 18px;
  line-height: 1.5;
  color: #555;
}

.contact-info a {
  text-decoration: none;
  color: #007BFF;
}

.contact-info a:hover {
  text-decoration: underline;
  color: #0056b3;
}

.contact-list {
  list-style-type: none;
  padding: 0;
}

.contact-list li {
  margin: 10px 0;
}

.contact-link {
  color: #007BFF;
  text-decoration: none;
}

.contact-link:hover {
  text-decoration: underline;
  color: #0056b3;
}

.copyright p {
  margin: 10px 0;
}

.copyright-link {
  text-decoration: none;
  color: #007BFF;
}

.copyright-link:hover {
  text-decoration: underline;
  color: #0056b3;
}
</style>
