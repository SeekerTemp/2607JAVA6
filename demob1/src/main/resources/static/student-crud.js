// LAB 5 - Bài 4: CRUD tương tác với Spring Boot REST API (StudentRestApi)
// API dạng danh sách: GET /students -> [student...], khóa chính là id của sinh viên
const host = "http://localhost:8081";

const $api = {
    // Đọc dữ liệu từ form
    get student() {
        return {
            id: $("#id").val(),
            name: $("#name").val(),
            mark: $("#mark").val(),
            gender: $("#male").prop("checked")
        };
    },

    // Hiển thị dữ liệu lên form
    set student(e) {
        e = e || {};
        $("#id").val(e.id || "");
        $("#name").val(e.name || "");
        $("#mark").val(e.mark || "");
        $("#male").prop("checked", !!e.gender);
        $("#female").prop("checked", !e.gender);
    },

    // Tải và hiển thị danh sách sinh viên lên bảng
    fillToTable() {
        var url = `${host}/students`;
        axios.get(url).then(resp => {
            $("tbody").empty();
            resp.data.forEach(e => {
                var tr = `<tr>
                    <td>${e.id}</td>
                    <td>${e.name}</td>
                    <td>${e.mark}</td>
                    <td>${e.gender ? 'Male' : 'Female'}</td>
                    <td>
                        <a href="#" onclick="$api.edit('${e.id}')">Edit</a>
                        <a href="#" onclick="$api.delete('${e.id}')">Delete</a>
                    </td>
                </tr>`;
                $("tbody").append(tr);
            });
        }).catch(error => {
            alert("Lỗi tải danh sách sinh viên!");
        });
    },

    // Tải sinh viên và hiển thị lên form
    edit(id) {
        var url = `${host}/students/${id}`;
        axios.get(url).then(resp => {
            this.student = resp.data;
        }).catch(error => {
            alert("Lỗi tải sinh viên!");
        });
    },

    // Thêm sinh viên mới
    create() {
        var url = `${host}/students`;
        axios.post(url, this.student).then(resp => {
            this.fillToTable();
            this.reset();
        }).catch(error => {
            alert("Lỗi thêm sinh viên mới!");
        });
    },

    // Cập nhật sinh viên
    update() {
        var s = this.student;
        var url = `${host}/students/${s.id}`;
        axios.put(url, s).then(resp => {
            this.fillToTable();
        }).catch(error => {
            alert("Lỗi cập nhật sinh viên!");
        });
    },

    // Xóa sinh viên
    delete(id) {
        var url = `${host}/students/${id || this.student.id}`;
        axios.delete(url).then(resp => {
            this.fillToTable();
            this.reset();
        }).catch(error => {
            alert("Lỗi xóa sinh viên!");
        });
    },

    // Xóa trắng form
    reset() {
        this.student = {};
    }
};

$api.fillToTable();
