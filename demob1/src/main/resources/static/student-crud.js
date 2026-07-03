// Thay bằng host Realtime Database của bạn
const host = "https://fpolyedu.firebaseio.com";

const $api = {
    key: null,

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
        var url = `${host}/students.json`;
        axios.get(url).then(resp => {
            $("tbody").empty();
            if (!resp.data) return;
            Object.keys(resp.data).forEach(key => {
                var e = resp.data[key];
                var tr = `<tr>
                    <td>${e.id}</td>
                    <td>${e.name}</td>
                    <td>${e.mark}</td>
                    <td>${e.gender ? 'Male' : 'Female'}</td>
                    <td>
                        <a href="#" onclick="$api.edit('${key}')">Edit</a>
                        <a href="#" onclick="$api.delete('${key}')">Delete</a>
                    </td>
                </tr>`;
                $("tbody").append(tr);
            });
        }).catch(error => {
            alert("Lỗi tải danh sách sinh viên!");
        });
    },

    // Tải sinh viên và hiển thị lên form
    edit(key) {
        this.key = key.trim();
        var url = `${host}/students/${key}.json`;
        axios.get(url).then(resp => {
            this.student = resp.data;
        }).catch(error => {
            alert("Lỗi tải sinh viên!");
        });
    },

    // Thêm sinh viên mới
    create() {
        var url = `${host}/students.json`;
        axios.post(url, this.student).then(resp => {
            this.fillToTable();
            this.reset();
        }).catch(error => {
            alert("Lỗi thêm sinh viên mới!");
        });
    },

    // Cập nhật thông tin sinh viên
    update() {
        var url = `${host}/students/${this.key}.json`;
        axios.put(url, this.student).then(resp => {
            this.fillToTable();
        }).catch(error => {
            alert("Lỗi cập nhật sinh viên!");
        });
    },

    // Xóa sinh viên
    delete(key) {
        var url = `${host}/students/${key || this.key}.json`;
        axios.delete(url).then(resp => {
            this.fillToTable();
            this.reset();
        }).catch(error => {
            alert("Lỗi xóa sinh viên!");
        });
    },

    // Xóa trắng form
    reset() {
        this.key = null;
        this.student = {};
    }
};

$api.fillToTable();
