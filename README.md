# Lab4 — Danh sách bạn bè

Ứng dụng Android viết bằng Kotlin, hiển thị danh sách bạn bè bằng RecyclerView.

## Chức năng

- Hiển thị ảnh, tên và mô tả của bạn bè.
- Hiển thị 5 phần tử mẫu từ `ListItem` bằng RecyclerView dạng danh sách dọc.
- Nút “Thêm mới” mở hộp thoại nhập tên và mô tả, thêm trực tiếp vào `ListItem`. Tên không được để trống. Dữ liệu thêm chỉ tồn tại trong bộ nhớ khi tiến trình ứng dụng còn chạy.

## Cấu trúc

- `ItemClass.kt`: model và danh sách mẫu.
- `MyHolder.kt`: giữ các View của một dòng.
- `MyAdapter.kt`: tạo và gán dữ liệu cho các dòng RecyclerView.
- `MainActivity.kt`: khởi tạo RecyclerView, LinearLayoutManager và MyAdapter.
- `itemlayout.xml`: giao diện một người bạn.

## Chạy project

Mở thư mục repo bằng Android Studio, cài Android SDK theo cấu hình Gradle và đồng bộ project. Chạy trên thiết bị hoặc emulator có API 35 trở lên.

1. Mở project `D:\AndroidStudioProjects` (thư mục chứa `settings.gradle.kts`).
2. Chọn **File > Sync Project with Gradle Files** và chờ hoàn tất.
3. Chọn cấu hình **app** trên thanh công cụ.
4. Chọn emulator đang chạy hoặc mở **Tools > Device Manager** để khởi động thiết bị API 35 trở lên.
5. Nhấn nút **Run ▶** hoặc **Shift+F10**.

Kết quả: màn hình có tiêu đề “Danh sách bạn bè”, 5 dòng Name1–Name5 và nút “Thêm mới”. Mỗi dòng có ảnh và mô tả. `anh` là alias của `anh1`, nên hai dòng đầu dùng cùng ảnh.

Trên Windows:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```

`local.properties`, thư mục build và cấu hình IDE riêng không được đưa vào Git.
