import { coursesServices } from "../services/coursesServices.js";

/** 
 *  @class
 * Classe que representa o curso no sistema 
 * 
*/
class Courses {
  constructor(
    title,
    type,
    category,
    img,
    description,
    address,
    zone,
    occupiedSlots,
    maxCapacity,
    partnerId,
    initialDate,
    endDate,
  ) {
    this.title = title;
    this.type = type;
    this.category = category;
    this.img = img;
    this.description = description;
    this.address = address;
    this.zone = zone;
    this.occupiedSlots = occupiedSlots;
    this.maxCapacity = maxCapacity;
    this.partnerId = partnerId;
    this.initialDate = initialDate;
    this.endDate = endDate;
  }
  /** Cria um novo curso no sistema. */
  async create() {
    const newCourses = {
      title: this.title,
      type: this.type,
      category: this.category,
      img: this.img,
      description: this.description,
      address: this.address,
      zone: this.zone,
      occupiedSlots: this.occupiedSlots,
      maxCapacity: this.maxCapacity,
      partnerId: this.partnerId,
      initialDate: this.initialDate,
      endDate: this.endDate,
    };
    return await coursesServices.createCourses(newCourses);
  }

  /** Método que pega o curso */
  static async getCourseById(courseId) {
    return await coursesServices.getCoursesById(courseId);
  }

  /** Método que atualiza o curso */
  static async updateCourseById(
    courseId,
    title,
    description,
    address,
    maxCapacity,
    initialDate,
    endDate,
    zone,
  ) {
    const putCourses = {
      title: this.title,
      type: this.type,
      category: this.category,
      img: this.img,
      description: this.description,
      address: this.address,
      zone: this.zone,
      occupiedSlots: this.occupiedSlots,
      maxCapacity: this.maxCapacity,
      partnerId: this.partnerId,
      initialDate: this.initialDate,
      endDate: this.endDate,
    };
    return await coursesServices.UpdateCourseById(putCourses);
  }
}

export default Courses;
