# kt1 — Bài kiểm tra lập trình di động

Project Android độc lập, viết bằng Kotlin và XML. Mở thư mục `kt1` bằng Android Studio, chờ Gradle Sync rồi chọn Run app. Yêu cầu Android SDK 37, JDK 17 trở lên tương thích Gradle 9.6; thiết bị Android 8.0 trở lên.

## Câu 1 — Giao diện và chức năng

- Màn hình chính có sẵn 3 câu hỏi, các thẻ màu tím nhạt, lựa chọn đáp án và nút Gửi bài kiểm tra theo mẫu.
- Chọn “+ Thêm câu hỏi” để mở biểu mẫu thêm mới; chọn “Sửa câu hỏi” tại một thẻ để sửa nội dung, bốn lựa chọn và đáp án đúng.
- Biểu mẫu kiểm tra các ô trống và yêu cầu chọn đáp án đúng. Hủy xóa toàn bộ nội dung nhập/chọn trên biểu mẫu và giữ nguyên màn hình. Dùng nút/cử chỉ Quay lại của hệ thống để về bài kiểm tra.
- Gửi bài yêu cầu trả lời đầy đủ, hiển thị số câu đúng và đáp án từng câu. “Làm lại” xóa lựa chọn hiện tại.
- Câu hỏi được lưu bằng SharedPreferences dưới dạng JSON và giữ lại sau khi đóng ứng dụng. Lựa chọn làm bài được giữ khi xoay màn hình, không lưu sau khi kết thúc phiên ứng dụng.

## Câu 2 — Giải thích xây dựng giao diện

`activity_main.xml` dùng LinearLayout theo chiều dọc để bố trí tiêu đề, nút thêm, vùng câu hỏi và nút gửi. ScrollView chứa danh sách câu hỏi để cuộn khi nội dung dài; layout_weight dành phần không gian còn lại cho vùng cuộn, giữ nút gửi ở cuối màn hình.

`MainActivity.showQuiz()` tạo một LinearLayout cho mỗi câu hỏi, dùng nền bo góc `card_background.xml`. TextView hiển thị câu hỏi. RadioGroup chứa bốn RadioButton, đảm bảo mỗi câu chỉ chọn được một đáp án. Sự kiện setOnCheckedChangeListener ghi nhận vị trí đáp án vào bản đồ answers; mỗi câu có nhóm riêng nên không ảnh hưởng câu khác.

`editor.xml` dùng ScrollView và LinearLayout chứa một EditText nhập câu hỏi, bốn EditText nhập lựa chọn, một RadioGroup chọn đáp án đúng A–D và hai Button Hủy/Lưu. Cùng biểu mẫu được dùng cho thêm và sửa: khi sửa, dữ liệu cũ được điền sẵn, tiêu đề và nhãn nút được đổi.

`showEditor()` kiểm tra dữ liệu khi bấm Lưu, tạo hoặc thay thế đối tượng Question rồi lưu JSON. Khi sửa câu hỏi, đáp án đang chọn của câu đó được xóa để người dùng trả lời lại. Nút gửi so sánh answers với thuộc tính correct để tính số câu đúng; AlertDialog hiển thị kết quả.

Màu chủ đạo được đặt trong styles.xml; drawable XML tạo nền tím và góc bo. Kích thước dùng dp, chữ dùng sp. WindowInsets cộng khoảng cách cho thanh trạng thái, thanh điều hướng và bàn phím. onSaveInstanceState giữ màn hình đang mở và lựa chọn khi Activity được tạo lại; Android tự phục hồi các trường nhập có ID cố định.

## Câu 3 — Xử lý sự kiện và giải thích

### 1. Nút Hủy xóa nội dung nhập/chọn (2 điểm)

Trong `setOnClickListener` của nút Hủy, duyệt năm EditText gồm câu hỏi và bốn lựa chọn. `field.text.clear()` xóa nội dung, `field.error = null` xóa thông báo lỗi cũ. `correctGroup.clearCheck()` bỏ lựa chọn A–D. `questionField.requestFocus()` đặt con trỏ về ô câu hỏi; Toast xác nhận đã xóa. Màn hình 02 vẫn mở để nhập lại. Khi đang sửa, thao tác này chỉ xóa bản nháp trong biểu mẫu, không thay đổi câu hỏi đã lưu cho đến khi bấm Lưu hợp lệ.

### 2. Thông báo khi bỏ trống ô nhập (2 điểm)

Khi bấm Thêm câu hỏi/Lưu thay đổi, gom năm EditText vào `allFields`, lọc bằng `isBlank()` để phát hiện cả chuỗi rỗng và chuỗi chỉ gồm khoảng trắng. Đặt `error` trên mọi ô thiếu, đưa con trỏ về ô thiếu đầu tiên và dùng Toast thông báo cần nhập đủ thông tin. `return@setOnClickListener` dừng lưu. Nếu các ô đủ nhưng `checkedRadioButtonId` không thuộc bốn ID đáp án, hiển thị thông báo yêu cầu chọn đáp án đúng. Chỉ khi hợp lệ mới tạo Question và lưu.

### 3. Kiểm tra câu chưa chọn khi gửi bài (2 điểm)

Mỗi RadioGroup ghi vị trí lựa chọn vào `answers` theo chỉ số câu hỏi. Khi bấm Gửi bài kiểm tra, duyệt toàn bộ `questions.indices`, tìm câu có `answers[it]` chưa nằm trong `0..3`. Nếu còn thiếu, AlertDialog liệt kê số câu chưa trả lời (đánh số từ 1), giữ nguyên các đáp án đã chọn và chưa chấm điểm. Khi không còn câu thiếu, so sánh từng lựa chọn với `correct`, đếm số câu đúng và hiển thị kết quả.

## Sửa cảnh báo

- Chuyển nhãn, gợi ý nhập và thông báo giao diện vào `res/values/strings.xml`; dùng tài nguyên có tham số cho kết quả và số câu hỏi.
- Thêm biểu tượng vector `ic_quiz.xml` và khai báo trong AndroidManifest.
- Đặt `importantForAutofill="no"` cho các ô soạn câu hỏi vì đây không phải dữ liệu tài khoản hay thông tin cá nhân cần tự điền.
- Dùng `ComponentActivity` và `OnBackPressedDispatcher` của AndroidX để xử lý Quay lại trên các phiên bản Android, bỏ hàm `onBackPressed()` cũ cùng chú thích bỏ qua lỗi lint.

## Kiểm tra thủ công

1. Mở ứng dụng, kiểm tra đủ 3 câu, cuộn và trả lời. Chọn lần lượt đáp án 1, 2, 3 để đạt 3/3.
2. Gửi khi chưa trả lời đủ để kiểm tra thông báo.
3. Thêm câu hỏi, thử bỏ trống nội dung hoặc không chọn đáp án đúng, sau đó lưu hợp lệ.
4. Sửa câu hỏi đã có; mở lại ứng dụng để kiểm tra dữ liệu đã lưu.
5. Nhập đủ năm ô và chọn đáp án, bấm Hủy: năm ô trống, không còn đáp án được chọn, lỗi nhập được xóa và vẫn ở màn hình 02. Dữ liệu câu hỏi đã lưu không bị xóa. Xoay màn hình để kiểm tra biểu mẫu vẫn trống.
6. Chỉ trả lời câu 2 rồi gửi: thông báo liệt kê câu 1, 3; chưa chấm điểm. Trả lời hết để kiểm tra chấm điểm.

Build trên Windows: `.\gradlew.bat assembleDebug lintDebug`.

## Rà soát Layout Validation

- `question_preview.xml` cung cấp câu hỏi và bốn lựa chọn đọc được cho Preview. Khi chạy, `showQuiz()` xóa mẫu XML bằng `removeAllViews()` rồi tạo danh sách từ dữ liệu đã lưu, không nhân đôi câu hỏi.
- Hai màn hình khai báo `fitsSystemWindows` để Preview bố trí trong vùng an toàn. Khi chạy, listener WindowInsetsCompat tính systemBars, displayCutout và ime rồi đặt padding từ giá trị gốc 16dp, không cộng dồn khi bàn phím mở/đóng.
- Nút Gửi bài dùng wrap_content, minHeight 48dp và căn giữa, tránh kéo dài quá mức ở màn hình rộng; chiều cao được phép tăng khi chữ lớn.
- Gradle lint và Layout Validation trong Android Studio là hai bộ kiểm tra khác nhau. Build/lint thành công không xác nhận rằng mọi cấu hình Preview đã qua Layout Validation. Sau thay đổi cần Build & Refresh Preview, kiểm tra màn hình nhỏ, nằm ngang và cỡ chữ lớn.
