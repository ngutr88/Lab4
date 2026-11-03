# Lab4 — Danh sách bạn bè

Ứng dụng Android viết bằng Kotlin, hiển thị danh sách bạn bè bằng RecyclerView.

## Chức năng

- Hiển thị ảnh, tên và mô tả của bạn bè.
- Thêm bạn bè qua hộp thoại, kiểm tra tên không được để trống.
- Lưu danh sách bằng SharedPreferences để sử dụng lại khi mở ứng dụng.

## Cấu trúc

- `ItemClass.kt`: model và danh sách mẫu.
- `MyHolder.kt`: giữ các View của một dòng.
- `MyAdapter.kt`: tạo và gán dữ liệu cho các dòng RecyclerView.
- `MainActivity.kt`: thiết lập màn hình, xử lý thêm và lưu dữ liệu.
- `itemlayout.xml`: giao diện một người bạn.

## Chạy project

Mở thư mục repo bằng Android Studio, cài Android SDK theo cấu hình Gradle và đồng bộ project. Chạy trên thiết bị hoặc emulator có API 35 trở lên.

Trên Windows:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```

`local.properties`, thư mục build và cấu hình IDE riêng không được đưa vào Git.
