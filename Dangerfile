# 変更した行以外は指摘しないように
# github.dismiss_out_of_range_messages

checkstyle_format.base_path = Dir.pwd
report_files = Dir.glob("**/detekt.xml")
for report_file in report_files do
    checkstyle_format.report report_file
end