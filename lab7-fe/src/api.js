import axios from "axios";

// Giống demovue: trỏ thẳng tới Spring Boot ở cổng 8081.
// Khi chạy `npm run dev` (cổng 5173) vẫn gọi được nhờ @CrossOrigin("*") ở controller.
export const api = axios.create({
    baseURL: "http://localhost:8081"
    ,headers:{
        "Content-Type":"application/json"
    }
});

// Lấy thông báo lỗi do REST API trả về, nếu không có thì dùng câu mặc định
export function apiError(err, fallback) {
    const data = err.response && err.response.data;
    return (typeof data === "string" && data) ? data : fallback;
}
