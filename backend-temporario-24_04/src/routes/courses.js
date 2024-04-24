import express from "express";
import { coursesController } from "../controllers/coursesController.js";

const router = express.Router();



router.get("/todos", coursesController.getAllCourses);
//router.delete("/:curso", deleteCourse);
//router.post("/criar", upload.single("image_url"), createCourse);
//router.put("/:curso", updateCourse);

export default router;
