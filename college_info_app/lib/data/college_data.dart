import '../models/course.dart';
import '../models/faculty.dart';

class CollegeData {
  static const String collegeName =
      "Vivekanand Education Society's Institute of Technology (VESIT)";
  static const String shortName = "VESIT, Chembur";
  static const String address =
      "Hashu Advani Memorial Complex, Collector's Colony, Chembur, Mumbai, Maharashtra 400074";
  static const String phone = "+91-22-61532510";
  static const String email = "vesit@ves.ac.in";
  static const String website = "https://vesit.ves.ac.in";

  static const List<Course> courses = [
    Course(
      code: "MCA",
      title: "Master of Computer Applications",
      department: "Department of MCA",
      duration: "2 Years (4 Semesters)",
      intake: 120,
      eligibility:
      "Passed BCA / B.Sc. (IT/CS) or Graduation with Mathematics at 10+2 level and valid MAH-MCA-CET score.",
      description:
      "Postgraduate program focusing on full-stack software engineering, artificial intelligence, cloud computing, and enterprise application development.",
    ),
    Course(
      code: "BE-CMPN",
      title: "B.E. in Computer Engineering",
      department: "Computer Engineering",
      duration: "4 Years (8 Semesters)",
      intake: 180,
      eligibility:
      "10+2 with Physics, Chemistry, and Mathematics along with valid MHT-CET or JEE Main score.",
      description:
      "Undergraduate engineering program covering data structures, operating systems, computer networks, and machine learning.",
    ),
    Course(
      code: "BE-INFT",
      title: "B.E. in Information Technology",
      department: "Information Technology",
      duration: "4 Years (8 Semesters)",
      intake: 120,
      eligibility:
      "10+2 with Physics, Chemistry, and Mathematics along with valid MHT-CET or JEE Main score.",
      description:
      "Focuses on software design, cybersecurity, database management systems, and distributed computing architectures.",
    ),
    Course(
      code: "BE-AIDS",
      title: "B.E. in AI & Data Science",
      department: "Artificial Intelligence & Data Science",
      duration: "4 Years (8 Semesters)",
      intake: 60,
      eligibility:
      "10+2 with Physics, Chemistry, and Mathematics along with valid MHT-CET or JEE Main score.",
      description:
      "Specialized curriculum in deep learning, natural language processing, big data analytics, and intelligent systems.",
    ),
  ];

  static const List<Faculty> facultyList = [
    Faculty(
      name: "Dr. (Mrs.) J. M. Nair",
      designation: "Principal & Professor",
      department: "Electronics Engineering",
      qualification: "Ph.D. (Tech), M.E.",
      specialization: "Signal Processing & Embedded Systems",
      email: "principal.vesit@ves.ac.in",
      imagePath: "assets/images/nair.jpg",
    ),
    Faculty(
      name: "Dr. Shivkumar Goel",
      designation: "Head of Department",
      department: "Department of MCA",
      qualification: "Ph.D., MCA",
      specialization: "Data Mining, Machine Learning & Software Engineering",
      email: "shivkumar.goel@ves.ac.in",
      imagePath: "assets/images/goel.jpg",
    ),
    Faculty(
      name: "Prof. Sunny Nahar",
      designation: "Assistant Professor",
      department: "Department of MCA",
      qualification: "M.Sc. (IT), MCA",
      specialization: "Artificial Intelligence, Computer Vision & Mobile Computing",
      email: "sunny.nahar@ves.ac.in",
      imagePath: "assets/images/nahar.jpg",
    ),
    Faculty(
      name: "Mrs. Ruchi Rautela",
      designation: "Assistant Professor",
      department: "Department of MCA",
      qualification: "M.C.A.",
      specialization: "Mobile Computing, Data Analytics, NLP & Machine Learning",
      email: "ruchi.rautela@ves.ac.in",
      imagePath: "assets/images/rautela.jpg",
    ),
  ];
}