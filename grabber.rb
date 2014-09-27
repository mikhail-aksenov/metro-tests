#!/usr/bin/env ruby

require 'translit'
require 'digest'

PFX="#{ENV['HOME']}/Development/university"

SOURCE="#{PFX}/metro\-tests/students_runnable.txt"
TEST_IN="{PFX}/metro\-tests/data/in1.txt"
TEST_OUT="#{PFX}/metro\-tests/data/ou1.txt"
TEST_TMP="#{PFX}/metro\-tests/data/tmp.txt"
TARGET="#{PFX}/students"

def grab(folder)
  puts "Removing folder #{folder}"
  %x(rm -rf #{folder})
  puts "Creating #{folder}"
  %x(mkdir -p #{folder})
  %x(cd #{folder} && git clone #{repo_path} #{folder})
end

def cmp_file(f1, f2)
  d1 = Digest::SHA256.file(f1) 
  d2 = Digest::SHA256.file(f2)
  d1.hexdigest == d2.hexdigest
end

def check_lab(name, folder)
  if File.exists? ("#{folder}/lab1.exe")
    tmp = "#{PFX}/students/#{name}/out.txt"
    %x(cd #{folder} && mono lab1.exe #{TEST_IN} #{tmp})
    puts("#{name}|n|#{cmp_file(tmp, TEST_OUT)}")
  else
    puts("#{name}|n|0")
  end
end

File.foreach(SOURCE) { |line| 
  nm, repo_path = line.split '|'
  name = Translit.convert(nm, :english).downcase.gsub(/\s/, '_').gsub(/'/, '')
  puts "Processing data for #{name}"
  folder = "#{TARGET}/#{name}"
  check_lab name, folder
  # grab folder
}